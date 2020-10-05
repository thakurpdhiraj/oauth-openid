package com.dhitha.springbootoauthserver.oauth.util;

import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_CLIENT;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_REQ_PARAMS;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_SCOPE_MAP;

import com.dhitha.springbootoauthserver.oauth.constant.AllowedScope;
import com.dhitha.springbootoauthserver.oauth.constant.Endpoints;
import com.dhitha.springbootoauthserver.oauth.dto.AuthorizeRequestDTO;
import com.dhitha.springbootoauthserver.oauth.entity.AuthorizationCode;
import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.entity.User;
import com.dhitha.springbootoauthserver.oauth.entity.UserOauthApproval;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericWebException;
import com.dhitha.springbootoauthserver.oauth.service.AuthorizationCodeService;
import com.dhitha.springbootoauthserver.oauth.service.UserOauthApprovalService;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.RedirectView;

/** @author Dhiraj */
@Component
public class AuthorizationUtil {

  private final UserOauthApprovalService userOauthApprovalService;

  private final AuthorizationCodeService authorizationCodeService;

  @Autowired
  public AuthorizationUtil(
      UserOauthApprovalService userOauthApprovalService,
      AuthorizationCodeService authorizationCodeService) {
    this.userOauthApprovalService = userOauthApprovalService;
    this.authorizationCodeService = authorizationCodeService;
  }

  public UserOauthApproval getUserRegisteredApproval(User loggedInUser, OauthClient client) {
    return userOauthApprovalService.find(loggedInUser, client);
  }

  public void saveUserApproval(OauthClient client, User loggedInUser, Set<String> scopes) {
    userOauthApprovalService.save(client, loggedInUser, scopes);
  }

  public AuthorizationCode saveCode(
      AuthorizeRequestDTO requestDTO, Set<String> scopes, OauthClient client, User user) {
    return authorizationCodeService.save(requestDTO, scopes, client, user);
  }

  public void validateParams(
      AuthorizeRequestDTO params, AuthorizeRequestDTO sessionParams, OauthClient client)
      throws GenericWebException {
    String redirectURI = Endpoints.OAUTH_ERROR_ENDPOINT;
    this.validateForRequestTampering(params, sessionParams, redirectURI);
    this.validateClient(sessionParams, client, redirectURI);
    this.validateRedirectURI(client, sessionParams, redirectURI);
    redirectURI = sessionParams.getRedirect_uri(); // valid client & redirect uri
    this.validateScopes(sessionParams, redirectURI);
    this.validateResponseType(sessionParams, redirectURI);
    this.validateAccessType(sessionParams, redirectURI);
  }

  private void validateClient(
      AuthorizeRequestDTO sessionParams, OauthClient client, String redirectURI)
      throws GenericWebException {
    if (!sessionParams.getClient_id().equals(client.getClientId())) {
      throw new GenericWebException("invalid_request", "Invalid Request", redirectURI);
    }
  }

  private void validateForRequestTampering(
      AuthorizeRequestDTO params, AuthorizeRequestDTO sessionParams, String redirectURI)
      throws GenericWebException {
    if (!params.equals(sessionParams)) {
      throw new GenericWebException("invalid_request", "Invalid Request", redirectURI);
    }
  }

  private void validateRedirectURI(
      OauthClient client, AuthorizeRequestDTO params, String redirectURI)
      throws GenericWebException {
    if (!client.getRedirectURIList().contains(params.getRedirect_uri())) {
      throw new GenericWebException(
          "invalid_request", "Invalid Parameter redirect_uri", redirectURI);
    }
  }

  private void validateScopes(AuthorizeRequestDTO params, String redirectURI)
      throws GenericWebException {
    Set<String> scopeSet =
        EnumSet.allOf(AllowedScope.class).stream()
            .map(AllowedScope::getValue)
            .collect(Collectors.toSet());
    if (!scopeSet.containsAll(params.getScope())) {
      throw new GenericWebException(
          "invalid_request",
          "Invalid scope parameter. Allowed scope: " + scopeSet,
          redirectURI,
          params.getState());
    }
  }

  private void validateResponseType(AuthorizeRequestDTO params, String redirectURI)
      throws GenericWebException {
    String responseType = params.getResponse_type();
    if (!responseType.equals("code")) {
      throw new GenericWebException(
          "invalid_request",
          "Invalid Parameter response_type : " + responseType + ". Only 'code' is allowed ",
          redirectURI,
          params.getState());
    }
  }

  private void validateAccessType(AuthorizeRequestDTO params, String redirectURI)
      throws GenericWebException {
    String accessType = params.getAccess_type();
    if (null != accessType && (!"offline".equals(accessType) && !"online".equals(accessType))) {
      throw new GenericWebException(
          "invalid_request",
          "Invalid 'access_type' parameter. Allowed: 'online', 'offline'",
          redirectURI,
          params.getState());
    }
  }

  public Map<String, Boolean> getMapOfScopes(Set<String> approvedScopes, Set<String> clientScope) {
    Set<String> allScopes = new HashSet<>();
    if (approvedScopes != null) {
      allScopes.addAll(approvedScopes);
    }
    if (clientScope != null) {
      allScopes.addAll(clientScope);
    }
    return allScopes.stream()
        .collect(
            Collectors.toMap(s -> s, s -> null != approvedScopes && approvedScopes.contains(s)));
  }

  public boolean hasUserApprovedAllRequestedScopes(Set<String> approvedScopes, Set<String> scopes) {
    return null != approvedScopes && approvedScopes.containsAll(scopes);
  }

  public RedirectView createRedirectView(
      String urlToRedirect, HttpServletResponse response, HttpServletRequest request) {
    request.getSession().removeAttribute(AUTH_REQ_ATTRIBUTE_REQ_PARAMS);
    request.getSession().removeAttribute(AUTH_REQ_ATTRIBUTE_CLIENT);
    request.getSession().removeAttribute(AUTH_REQ_ATTRIBUTE_SCOPE_MAP);
    RedirectView redirectView = new RedirectView(urlToRedirect);
    redirectView.setEncodingScheme(StandardCharsets.UTF_8.toString());
    redirectView.setStatusCode(HttpStatus.FOUND);
    return redirectView;
  }

  public String createSuccessAuthRedirectURI(AuthorizationCode code, AuthorizeRequestDTO params) {
    StringBuilder redirect = new StringBuilder(params.getRedirect_uri());
    redirect.append("?code=").append(code.getCode());
    if (params.getState() != null) {
      redirect.append("&state=").append(params.getState());
    }
    return redirect.toString();
  }
}

package com.dhitha.springbootoauthserver.oauth.util;

import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_CLIENT;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_REQ_PARAMS;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_SCOPE_MAP;

import com.dhitha.springbootoauthserver.oauth.constant.AllowedScope;
import com.dhitha.springbootoauthserver.oauth.dto.AuthorizeRequestDTO;
import com.dhitha.springbootoauthserver.oauth.entity.AuthorizationCode;
import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.entity.User;
import com.dhitha.springbootoauthserver.oauth.entity.UserOauthApproval;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericAuthorizationException;
import com.dhitha.springbootoauthserver.oauth.error.notfound.OauthClientNotFoundException;
import com.dhitha.springbootoauthserver.oauth.service.AuthorizationCodeService;
import com.dhitha.springbootoauthserver.oauth.service.OauthClientService;
import com.dhitha.springbootoauthserver.oauth.service.UserOauthApprovalService;
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

  private final OauthClientService oauthClientService;

  private final UserOauthApprovalService userOauthApprovalService;

  private final AuthorizationCodeService authorizationCodeService;

  @Autowired
  public AuthorizationUtil(
      OauthClientService oauthClientService,
      UserOauthApprovalService userOauthApprovalService,
      AuthorizationCodeService authorizationCodeService) {
    this.oauthClientService = oauthClientService;
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

  public void validateForRequestTampering(AuthorizeRequestDTO params, AuthorizeRequestDTO reqParams)
      throws GenericAuthorizationException {
    if (!params.equals(reqParams)) {
      throw new GenericAuthorizationException("Invalid Request");
    }
  }

  public void validateResponseType(AuthorizeRequestDTO params)
      throws GenericAuthorizationException {
    String responseType = params.getResponse_type();
    if (!responseType.equals("code")) {
      throw new GenericAuthorizationException(
          "Invalid Parameter response_type : " + responseType + ". Only 'code' is allowed ");
    }
  }

  public void validateRedirectURI(OauthClient client, AuthorizeRequestDTO params)
      throws GenericAuthorizationException {
    String redirectURI = params.getRedirect_uri();
    if (!client.getRedirectURIList().contains(redirectURI)) {
      throw new GenericAuthorizationException("Invalid Parameter redirect_uri : " + redirectURI);
    }
  }

  public void validateScopes(AuthorizeRequestDTO params) throws GenericAuthorizationException {
    Set<String> scopeSet =
        EnumSet.allOf(AllowedScope.class).stream()
            .map(AllowedScope::getValue)
            .collect(Collectors.toSet());
    if (!scopeSet.containsAll(params.getScope())) {
      throw new GenericAuthorizationException(
          "Invalid scope parameter. Allowed scope: " + scopeSet);
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
    response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
    response.setHeader("Pragma", "no-cache");
    RedirectView redirectView = new RedirectView(urlToRedirect);
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

  public OauthClient findByClientId(String clientId) throws GenericAuthorizationException {
    try {
      return oauthClientService.findByClientId(clientId);
    } catch (OauthClientNotFoundException e) {
      throw new GenericAuthorizationException(e.getMessage());
    }
  }

  public void validateAccessType(AuthorizeRequestDTO params) throws GenericAuthorizationException {
    String accessType = params.getAccess_type();
    if (null != accessType && (!"offline".equals(accessType) && !"online".equals(accessType))) {
      throw new GenericAuthorizationException(
          "Invalid 'access_type' parameter. Allowed: 'online', 'offline'");
    }
  }
}

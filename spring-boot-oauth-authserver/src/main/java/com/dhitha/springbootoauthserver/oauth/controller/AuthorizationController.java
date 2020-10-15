package com.dhitha.springbootoauthserver.oauth.controller;

import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_CLIENT;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_REQ_PARAMS;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_SCOPE_MAP;

import com.dhitha.springbootoauthserver.oauth.constant.Endpoints;
import com.dhitha.springbootoauthserver.oauth.dto.AuthorizeRequestDTO;
import com.dhitha.springbootoauthserver.oauth.entity.AuthorizationCode;
import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.resource.entity.User;
import com.dhitha.springbootoauthserver.oauth.entity.UserOauthApproval;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericWebException;
import com.dhitha.springbootoauthserver.oauth.util.AuthorizationUtil;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to handle creation and validation of authorization code flow
 *
 * @author Dhiraj
 */
@Controller
@RequestMapping(Endpoints.AUTHORIZATION_ENDPOINT)
@Log4j2
public class AuthorizationController {

  private final AuthorizationUtil authorizationUtil;

  @Autowired
  public AuthorizationController(AuthorizationUtil authorizationUtil) {
    this.authorizationUtil = authorizationUtil;
  }

  // http://localhost:8081/oauth/v1/authorize?client_id=app.lms.1&redirect_uri=http:%2F%2Flocalhost:8181%2Fwholesale&response_type=code&scope=openid;read.user&state=50111
  /* ****************************GET-Show Authorize Page****************************** */
  @GetMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  public Object redirectToAuthorize(
      @AuthenticationPrincipal User loggedInUser,
      HttpServletRequest request,
      HttpServletResponse response,
      @Valid AuthorizeRequestDTO params,
      BindingResult bindingResult)
      throws GenericWebException {
    final String LOG_METHOD = "redirectToAuthorize(): ";
    log.info(
        LOG_METHOD + "Principal: {} is processing for authorization for: {}", loggedInUser, params);
    if (bindingResult.hasErrors()) {
      throw new GenericWebException(bindingResult);
    }
    AuthorizeRequestDTO reqParams =
        (AuthorizeRequestDTO) request.getSession().getAttribute(AUTH_REQ_ATTRIBUTE_REQ_PARAMS);
    log.info(LOG_METHOD + "Parameter in Session request: {}", reqParams);
    OauthClient client = (OauthClient) request.getSession().getAttribute(AUTH_REQ_ATTRIBUTE_CLIENT);
    log.info(LOG_METHOD + "Client in Session request: {}", client);
    authorizationUtil.validateParams(params, reqParams, client);

    UserOauthApproval userApprovals =
        authorizationUtil.getUserRegisteredApproval(loggedInUser, client);
    log.info(LOG_METHOD + "UserOauthApproval: {}", userApprovals);
    boolean requestedScopesApproved =
        authorizationUtil.hasUserApprovedAllRequestedScopes(
            userApprovals.getApprovedScopes(), params.getScope());
    log.info(LOG_METHOD + "userApprovedAllRequestedScopes: {}", requestedScopesApproved);

    if (!requestedScopesApproved) {
      Map<String, Boolean> scopeMap =
          authorizationUtil.getMapOfScopes(userApprovals.getApprovedScopes(), params.getScope());
      request.getSession().setAttribute(AUTH_REQ_ATTRIBUTE_SCOPE_MAP, scopeMap);

      ModelAndView mv = new ModelAndView("oauth_authorize");
      mv.addObject("post_url", Endpoints.AUTHORIZATION_ENDPOINT);
      return mv;
    } else {
      AuthorizationCode code =
          authorizationUtil.saveCode(reqParams, params.getScope(), client, loggedInUser);
      String redirectURL = authorizationUtil.createSuccessAuthRedirectURI(code, reqParams);
      return authorizationUtil.createRedirectView(redirectURL, response, request);
    }
  }

  /* *********************POST-> Save and send authorization code****************************** */

  @SuppressWarnings("unchecked")
  @PostMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  public Object submitAuthRequest(
      @AuthenticationPrincipal User loggedInUser,
      HttpServletRequest request,
      HttpServletResponse response)
      throws GenericWebException {
    final String LOG_METHOD = "submitAuthRequest(): ";
    String decision = request.getParameter("decision");
    log.info(LOG_METHOD + "decision: {}", decision);
    String redirectURI;
    AuthorizeRequestDTO reqParams =
        (AuthorizeRequestDTO) request.getSession().getAttribute(AUTH_REQ_ATTRIBUTE_REQ_PARAMS);
    switch (decision) {
      case "authorize":
        OauthClient client =
            (OauthClient) request.getSession().getAttribute(AUTH_REQ_ATTRIBUTE_CLIENT);
        Map<String, Boolean> scopeMap =
            (HashMap<String, Boolean>)
                request.getSession().getAttribute(AUTH_REQ_ATTRIBUTE_SCOPE_MAP);

        authorizationUtil.saveUserApproval(client, loggedInUser, scopeMap.keySet());
        AuthorizationCode code =
            authorizationUtil.saveCode(reqParams, scopeMap.keySet(), client, loggedInUser);

        redirectURI = authorizationUtil.createSuccessAuthRedirectURI(code, reqParams);
        return authorizationUtil.createRedirectView(redirectURI, response, request);

      case "cancel":
        String error = "access_denied";
        String errorDescription = "The user rejected permission to the requested scope";
        throw new GenericWebException(
            error, errorDescription, reqParams.getRedirect_uri(), reqParams.getState());

      default:
        throw new GenericWebException(
            "invalid_request", "Invalid Request", Endpoints.OAUTH_ERROR_ENDPOINT);
    }
  }
}

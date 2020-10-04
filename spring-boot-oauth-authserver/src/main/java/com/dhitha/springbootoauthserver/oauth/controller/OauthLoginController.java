package com.dhitha.springbootoauthserver.oauth.controller;

import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_CLIENT;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_REQ_PARAMS;

import com.dhitha.springbootoauthserver.oauth.constant.Endpoints;
import com.dhitha.springbootoauthserver.oauth.dto.AuthorizeRequestDTO;
import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericWebException;
import com.dhitha.springbootoauthserver.oauth.error.notfound.OauthClientNotFoundException;
import com.dhitha.springbootoauthserver.oauth.service.OauthClientService;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Map the login controller for OAuth
 *
 * @author Dhiraj
 */
@Controller
@Log4j2
public class OauthLoginController {
  private final OauthClientService oauthClientService;

  private final Validator validator;

  @Autowired
  public OauthLoginController(OauthClientService oauthClientService, Validator validator) {
    this.oauthClientService = oauthClientService;
    this.validator = validator;
  }

  // http://localhost:8081/oauth/v1/authorize?client_id=app.lms.1&redirect_uri=http:%2F%2Flocalhost:8181%2Fwholesale&response_type=code&scope=openid&state=50111
  @GetMapping(Endpoints.LOGIN_ENDPOINT)
  public String redirectToLogin(
      HttpServletRequest request, HttpServletResponse response, Model model)
      throws GenericWebException {
    try {
      AuthorizeRequestDTO oauthAuthorizeRequestDTO = validateAndFetchRequestParam(request);
      OauthClient oauthClient = this.findByClientId(oauthAuthorizeRequestDTO.getClient_id());
      request.getSession().setAttribute(AUTH_REQ_ATTRIBUTE_CLIENT, oauthClient);
      return "oauth_login";
    } catch (Exception e) {
      log.error("Error while api login: ",e);
      throw new GenericWebException(e.getMessage());
    }
  }

  private AuthorizeRequestDTO validateAndFetchRequestParam(HttpServletRequest request)
      throws GenericWebException {
    if (request == null) {
      throw new GenericWebException();
    }
    AuthorizeRequestDTO oauthAuthorizeRequestDTO =
        (AuthorizeRequestDTO) request.getSession().getAttribute(AUTH_REQ_ATTRIBUTE_REQ_PARAMS);
    Set<ConstraintViolation<AuthorizeRequestDTO>> validate =
        validator.validate(oauthAuthorizeRequestDTO, Default.class);
    if (!validate.isEmpty()) {
      String validationException =
          validate.stream()
              .map(cv -> cv == null ? "null" : cv.getPropertyPath() + ": " + cv.getMessage())
              .collect(Collectors.joining(", "));
      throw new GenericWebException(validationException);
    }
    return oauthAuthorizeRequestDTO;
  }

  private OauthClient findByClientId(String clientId) throws GenericWebException {
    try {
      return oauthClientService.findByClientId(clientId);
    } catch (OauthClientNotFoundException e) {
      throw new GenericWebException(e.getMessage());
    }
  }
}

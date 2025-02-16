package com.dhitha.springbootoauthserver.oauth.error.handler;

import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_CLIENT;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_REQ_PARAMS;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_SCOPE_MAP;

import com.dhitha.springbootoauthserver.oauth.error.generic.GenericAPIException;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericWebException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Generic Exception Handler to manage API & WEB exceptions
 *
 * @author Dhiraj
 */
@ControllerAdvice
public class OauthExceptionHandler {

  @ExceptionHandler(value = {GenericAPIException.class})
  public ResponseEntity<?> handleTokenError(GenericAPIException e) {
    JSONObject error =
        new JSONObject()
            .appendField("error", e.getMessage())
            .appendField("error_description", e.getDescription());
    return ResponseEntity.status(e.getStatus()).body(error);
  }

  @ExceptionHandler(value = {GenericWebException.class})
  public RedirectView handleError(
      GenericWebException e, HttpServletRequest request, HttpServletResponse response) {
    cleanUpSessionAttributes(request);
    StringBuilder redirect = new StringBuilder();
    redirect
        .append(e.getRedirectUri())
        .append("?error=")
        .append(e.getError())
        .append("&error_description=")
        .append(e.getErrorDescription());
    if (e.getState() != null) {
      redirect.append("&state=").append(e.getState());
    }
    RedirectView redirectView = new RedirectView(redirect.toString());
    redirectView.setEncodingScheme(StandardCharsets.UTF_8.toString());
    redirectView.setStatusCode(HttpStatus.FOUND);
    return redirectView;
  }

  private void cleanUpSessionAttributes(HttpServletRequest request) {
    request.getSession().removeAttribute(AUTH_REQ_ATTRIBUTE_REQ_PARAMS);
    request.getSession().removeAttribute(AUTH_REQ_ATTRIBUTE_CLIENT);
    request.getSession().removeAttribute(AUTH_REQ_ATTRIBUTE_SCOPE_MAP);
  }
}

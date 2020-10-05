package com.dhitha.springbootoauthserver.oauth.error.handler;

import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_CLIENT;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_REQ_PARAMS;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_SCOPE_MAP;

import com.dhitha.springbootoauthserver.oauth.error.generic.GenericAPIException;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericWebException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

/** @author Dhiraj */
@ControllerAdvice
public class OauthExceptionHandler {

  @ExceptionHandler(value = {GenericAPIException.class})
  public ResponseEntity<?> handleTokenError(GenericAPIException e) {
    JSONObject error = new JSONObject().appendField(e.getMessage(), e.getDescription());
    return ResponseEntity.status(e.getStatus()).body(error);
  }

  @ExceptionHandler(value = {GenericWebException.class})
  public RedirectView handleError(
      GenericWebException e, HttpServletRequest request, HttpServletResponse response) {
    cleanUpSessionAttributes(request);
    StringBuilder redirect = new StringBuilder();
    redirect.append(e.getRedirectUri())
        .append(URLEncoder.encode("?error=", StandardCharsets.UTF_8))
        .append(URLEncoder.encode(e.getError(), StandardCharsets.UTF_8))
        .append(URLEncoder.encode("&error_description=", StandardCharsets.UTF_8))
        .append(URLEncoder.encode(e.getErrorDescription(), StandardCharsets.UTF_8));
    if(e.getState() != null){
      redirect.append(URLEncoder.encode("&state=", StandardCharsets.UTF_8))
          .append(URLEncoder.encode(e.getState(), StandardCharsets.UTF_8));
    }
    RedirectView redirectView =
        new RedirectView(redirect.toString());
    redirectView.setStatusCode(HttpStatus.FOUND);
    return redirectView;
  }

  private void cleanUpSessionAttributes(HttpServletRequest request) {
    request.getSession().removeAttribute(AUTH_REQ_ATTRIBUTE_REQ_PARAMS);
    request.getSession().removeAttribute(AUTH_REQ_ATTRIBUTE_CLIENT);
    request.getSession().removeAttribute(AUTH_REQ_ATTRIBUTE_SCOPE_MAP);
  }
}

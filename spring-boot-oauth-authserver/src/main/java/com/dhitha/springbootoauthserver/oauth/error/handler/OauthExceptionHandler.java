package com.dhitha.springbootoauthserver.oauth.error.handler;

import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_CLIENT;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_REQ_PARAMS;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_SCOPE_MAP;

import com.dhitha.springbootoauthserver.oauth.constant.Endpoints;
import com.dhitha.springbootoauthserver.oauth.dto.TokenErrorDTO;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericAPIException;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericWebException;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/** @author Dhiraj */
@ControllerAdvice
public class OauthExceptionHandler {

  @ExceptionHandler(value = {GenericAPIException.class})
  public ResponseEntity<?> handleTokenError(GenericAPIException e) {
    TokenErrorDTO error = new TokenErrorDTO(e.getMessage(), e.getDescription());
    return ResponseEntity.status(e.getStatus()).body(error);
  }

  @ExceptionHandler(value = {GenericWebException.class})
  public ModelAndView handleError(
      GenericWebException e, HttpServletRequest request, HttpServletResponse response) {
    cleanUpSessionAttributes(request);
    ModelAndView mv = new ModelAndView("oauth_error");
    mv.addObject("errors", Collections.singletonList(e.getMessage()));
    return mv;
  }

  @ExceptionHandler(value = Exception.class)
  public Object handleGenericError(Exception e, HttpServletRequest request) {
    if (request.getServletPath().equals(Endpoints.TOKEN_ENDPOINT)) {
      HttpStatus eStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      if (e instanceof MissingRequestHeaderException
          || e instanceof UnsatisfiedServletRequestParameterException) {
        eStatus = HttpStatus.BAD_REQUEST;
      }
      TokenErrorDTO error = new TokenErrorDTO(e.getMessage(), e.getLocalizedMessage());
      return ResponseEntity.status(eStatus).body(error);
    } else {
      cleanUpSessionAttributes(request);
      ModelAndView mv = new ModelAndView("error");
      mv.addObject("errors", "Something went wrong");
      return mv;
    }
  }

  private void cleanUpSessionAttributes(HttpServletRequest request) {
    request.getSession().removeAttribute(AUTH_REQ_ATTRIBUTE_REQ_PARAMS);
    request.getSession().removeAttribute(AUTH_REQ_ATTRIBUTE_CLIENT);
    request.getSession().removeAttribute(AUTH_REQ_ATTRIBUTE_SCOPE_MAP);
  }
}

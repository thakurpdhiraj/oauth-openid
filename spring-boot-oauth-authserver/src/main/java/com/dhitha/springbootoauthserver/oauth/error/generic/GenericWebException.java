package com.dhitha.springbootoauthserver.oauth.error.generic;

import com.dhitha.springbootoauthserver.oauth.constant.Endpoints;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

/**
 * Generic Web Exception which requires redirection with errors appended as query params
 *
 * @author Dhiraj
 */
public class GenericWebException extends Exception {
  private final String error;
  private final String errorDescription;
  private final String redirectUri;
  private final String state;

  public GenericWebException(String error, String errorDescription, String redirectUri) {
    super(errorDescription);
    this.error = error;
    this.errorDescription = errorDescription;
    this.redirectUri = redirectUri;
    this.state = null;
  }

  public GenericWebException(
      String error, String errorDescription, String redirectUri, String state) {
    super(errorDescription);
    this.error = error;
    this.errorDescription = errorDescription;
    this.redirectUri = redirectUri;
    this.state = state;
  }

  public GenericWebException(BindingResult bindingResult) {
    super("invalid_request");
    this.error = "invalid_request";
    this.errorDescription =
        bindingResult.getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(","));
    this.redirectUri = Endpoints.OAUTH_ERROR_ENDPOINT;
    this.state = null;
  }

  public String getError() {
    return error;
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public String getState() {
    return state;
  }
}

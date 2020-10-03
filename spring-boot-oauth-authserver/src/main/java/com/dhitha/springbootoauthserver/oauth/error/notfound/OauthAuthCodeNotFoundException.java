package com.dhitha.springbootoauthserver.oauth.error.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** @author Dhiraj */
public class OauthAuthCodeNotFoundException extends Exception {

  private final HttpStatus status = HttpStatus.NOT_FOUND;

  public OauthAuthCodeNotFoundException(String message) {
    super(message);
  }

  public HttpStatus getStatus() {
    return status;
  }
}

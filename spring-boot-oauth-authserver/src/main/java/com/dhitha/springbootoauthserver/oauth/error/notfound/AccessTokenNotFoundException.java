package com.dhitha.springbootoauthserver.oauth.error.notfound;

import org.springframework.http.HttpStatus;

/**
 * @author Dhiraj
 */
public class AccessTokenNotFoundException extends Exception {

  private final HttpStatus status = HttpStatus.NOT_FOUND;

  public AccessTokenNotFoundException(String message) {
    super(message);
  }

  public HttpStatus getStatus() {
    return status;
  }
}
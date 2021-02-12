package com.dhitha.springbootoauthserver.oauth.error.notfound;

import org.springframework.http.HttpStatus;

/**
 * When access token is not found in the system corresponding to Status code 404
 *
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

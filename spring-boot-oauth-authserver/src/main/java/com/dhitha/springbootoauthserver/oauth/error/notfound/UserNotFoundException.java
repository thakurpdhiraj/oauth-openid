package com.dhitha.springbootoauthserver.oauth.error.notfound;

import org.springframework.http.HttpStatus;

/**
 * User not found exception corresponding to Status code 404.
 *
 * @author Dhiraj
 */
public class UserNotFoundException extends Exception {
  private final HttpStatus status = HttpStatus.NOT_FOUND;

  public UserNotFoundException(String message) {
    super(message);
  }

  public HttpStatus getStatus() {
    return status;
  }
}

package com.dhitha.springbootoauthserver.oauth.error.notfound;

import org.springframework.http.HttpStatus;

/**
 * Client not found exception corresponding to Status code 404
 *
 * @author Dhiraj
 */
public class OauthClientNotFoundException extends Exception {

  private final HttpStatus status = HttpStatus.NOT_FOUND;

  public OauthClientNotFoundException(String message) {
    super(message);
  }

  public HttpStatus getStatus() {
    return status;
  }
}

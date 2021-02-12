package com.dhitha.springbootoauthserver.oauth.error.notfound;

import org.springframework.http.HttpStatus;

/**
 * When authorization code is not found or expired corresponding to Status code 404
 *
 * @author Dhiraj
 */
public class OauthAuthCodeNotFoundException extends Exception {

  private final HttpStatus status = HttpStatus.NOT_FOUND;

  public OauthAuthCodeNotFoundException(String message) {
    super(message);
  }

  public HttpStatus getStatus() {
    return status;
  }
}

package com.dhitha.springbootoauthserver.oauth.error.generic;

import org.springframework.http.HttpStatus;

/**
 * Generic Exception when ResponseEntity is required as response
 * @author Dhiraj
 */
public class GenericAPIException extends Exception{
  private final String description;
  private final HttpStatus status;

  public GenericAPIException() {
    super("internal_error");
    this.description = "Something went wrong";
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public GenericAPIException(String message, String description, HttpStatus status) {
    super(message);
    this.description = description;
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public HttpStatus getStatus() {
    return status;
  }
}

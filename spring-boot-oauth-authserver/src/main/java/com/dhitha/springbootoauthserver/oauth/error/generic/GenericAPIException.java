package com.dhitha.springbootoauthserver.oauth.error.generic;

import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

/**
 * Generic Exception when ResponseEntity is required as response
 *
 * @author Dhiraj
 */
public class GenericAPIException extends Exception {
  private final String description;
  private final HttpStatus status;

  public GenericAPIException() {
    super("server_error");
    this.description = "Something went wrong";
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public GenericAPIException(BindingResult bindingResult) {
    super("invalid_request");
    this.description =
        bindingResult.getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(","));
    this.status = HttpStatus.BAD_REQUEST;
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

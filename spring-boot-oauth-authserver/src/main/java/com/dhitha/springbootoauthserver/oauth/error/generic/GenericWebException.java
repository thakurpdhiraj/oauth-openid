package com.dhitha.springbootoauthserver.oauth.error.generic;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

/**
 * @author Dhiraj
 */
public class GenericWebException extends Exception{
  private final String description;

  public GenericWebException() {
    super("server_error");
    this.description = "Something went wrong";
  }

  public GenericWebException(String description) {
    super("invalid_request");
    this.description = description;
  }

  public GenericWebException(BindingResult bindingResult){
    super("invalid_request");
    this.description = bindingResult.getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.joining(","));
  }

  public GenericWebException(List<String> messages) {
    super("invalid_request");
    this.description = String.join(", ", messages);
  }

  public String getDescription() {
    return description;
  }
}

package com.dhitha.springbootoauthserver.oauth.error.generic;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

/**
 * Generic Exception for Token Generation
 *
 * @author Dhiraj
 */
public class GenericTokenException extends GenericAPIException {

  public GenericTokenException() {
    super();
  }

  public GenericTokenException(BindingResult bindingResult) {
    super(
        "invalid_request",
        String.join(",", getBindingResultErrors(bindingResult)),
        HttpStatus.BAD_REQUEST);
  }

  public GenericTokenException(String message, String description, HttpStatus status) {
    super(message, description, status);
  }

  private static List<String> getBindingResultErrors(BindingResult bindingResult) {
    return bindingResult.getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());
  }
}

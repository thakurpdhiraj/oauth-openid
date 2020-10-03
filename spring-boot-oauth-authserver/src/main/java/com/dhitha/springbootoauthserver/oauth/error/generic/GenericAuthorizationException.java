package com.dhitha.springbootoauthserver.oauth.error.generic;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

/** @author Dhiraj */
public class GenericAuthorizationException extends GenericWebException {

  public GenericAuthorizationException(String message) {
    super(message);
  }

  public GenericAuthorizationException(List<String> messages) {
    super(String.join(", ", messages));
  }

  public GenericAuthorizationException(BindingResult bindingResult) {
    this(getBindingResultErrors(bindingResult));
  }

  private static List<String> getBindingResultErrors(BindingResult bindingResult) {
    return bindingResult.getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());
  }
}

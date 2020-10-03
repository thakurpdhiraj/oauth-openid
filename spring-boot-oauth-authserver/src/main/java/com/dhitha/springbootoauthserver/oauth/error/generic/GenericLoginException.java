package com.dhitha.springbootoauthserver.oauth.error.generic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Dhiraj
 */
public class GenericLoginException extends GenericWebException {

  public GenericLoginException() {
    super();
  }

  public GenericLoginException(String message) {
    super(message);
  }
}

package com.dhitha.springbootoauthserver.oauth.error.generic;

import org.springframework.http.HttpStatus;

/**
 * @author Dhiraj
 */
public class GenericWebException extends Exception{

  public GenericWebException() {
    super("Something went wrong. Please contact administrators");
  }

  public GenericWebException(String message) {
    super(message);
  }
}

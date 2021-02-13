package com.dhitha.springbootoauthserver.oauth.constant;

/**
 * Allowed Scopes for User
 *
 * @author Dhiraj
 */
public enum AllowedScope {
  OPENID("openid"),
  PROFILE("profile"),
  EMAIL("email");

  private final String value;

  AllowedScope(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

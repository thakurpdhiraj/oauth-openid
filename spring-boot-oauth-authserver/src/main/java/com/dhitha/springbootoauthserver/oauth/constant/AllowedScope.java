package com.dhitha.springbootoauthserver.oauth.constant;

public enum AllowedScope {
  OPENID("openid"),
  PROFILE("profile"),
  READ_USER_BOOK("read.user.book"),
  CREATE_USER_BOOK("create.user.book"),
  READ_BOOK("read.book"),
  CREATE_BOOK("create.book");

  private final String value;

  AllowedScope(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}

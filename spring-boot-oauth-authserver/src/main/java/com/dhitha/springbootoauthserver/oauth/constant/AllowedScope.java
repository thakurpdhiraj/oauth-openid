package com.dhitha.springbootoauthserver.oauth.constant;

public enum AllowedScope {
  OPENID("openid"),
  PROFILE("profile"),
  READ_USER_BOOK("http://localhost:8082/lms/api/read.user.book"),
  CREATE_USER_BOOK("http://localhost:8082/lms/api/create.user.book"),
  READ_BOOK("http://localhost:8082/lms/api/read.book"),
  CREATE_BOOK("http://localhost:8082/lms/api/create.book");

  private final String value;

  AllowedScope(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}

package com.dhitha.springbootoauthserver.oauth.constant;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum AllowedScope {
  OPENID("openid"),
  PROFILE("profile"),
  READ_USER_BOOK("read.user.book"),
  CREATE_USER_BOOK("create.user.book"),
  READ_BOOK("read.book"),
  CREATE_BOOK("create.book");

  String value;

  AllowedScope(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static String getAllAsString(){
   return Arrays.stream(AllowedScope.values())
        .map(AllowedScope::getValue)
        .collect(Collectors.joining(","));
  }
}

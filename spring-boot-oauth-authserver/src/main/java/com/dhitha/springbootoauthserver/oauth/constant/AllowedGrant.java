package com.dhitha.springbootoauthserver.oauth.constant;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

public enum AllowedGrant {
  AUTHORIZATION_CODE("authorization_code"),
  REFRESH_TOKEN("refresh_token");

  private final String value;

  AllowedGrant(String value) {
    this.value = value;
  }

  public static AllowedGrant get(String grant) {
    return Arrays.stream(AllowedGrant.values())
        .filter(allowedGrant -> allowedGrant.getValue().equals(grant))
        .findFirst()
        .orElseThrow(()-> new IllegalArgumentException(
            "the 'grant_type' is invalid. Allowed grant_type values : " + getAllAsString()));
  }

  public static String getAllAsString() {
    return EnumSet.allOf(AllowedGrant.class).stream()
        .map(AllowedGrant::getValue)
        .collect(Collectors.joining(", "));
  }

  public String getValue() {
    return value;
  }
}

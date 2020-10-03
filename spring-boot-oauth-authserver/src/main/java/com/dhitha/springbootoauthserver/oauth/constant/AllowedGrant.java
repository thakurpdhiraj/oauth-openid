package com.dhitha.springbootoauthserver.oauth.constant;

import java.util.EnumSet;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

public enum AllowedGrant {
  AUTHORIZATION_CODE("authorization_code"),
  REFRESH_TOKEN("refresh_token");

  String value;

  AllowedGrant(String value) {
    this.value = value;
  }

  public static boolean contains(String grant) {
    for(AllowedGrant allowedGrant: AllowedGrant.values()){
      if(allowedGrant.getValue().equals(grant)){
        return true;
      }
    }
    return false;
  }

  public String getValue() {
    return value;
  }

  public static String getAllAsString(){
    return  EnumSet.allOf(AllowedGrant.class).stream().map(AllowedGrant::getValue).collect(Collectors.joining(", "));
  }
}

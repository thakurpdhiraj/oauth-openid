package com.dhitha.springbootoauthserver.oauth.constant;

/**
 * All endpoints for application
 *
 * @author Dhiraj
 */
public class Endpoints {

  public static final String AUTHORIZATION_ENDPOINT = "/oauth/v1/authorize";
  public static final String TOKEN_ENDPOINT = "/oauth/v1/token";
  public static final String LOGIN_ENDPOINT = "/oauth/v1/login";
  public static final String USERINFO_ENDPOINT = "/oauth/v1/userinfo";
  public static final String CERTS_ENDPOINT = "/oauth/v1/certs";
  public static final String WELL_KNOWN_ENDPOINT = "/.well-known/openid-configuration";
  public static final String OAUTH_ERROR_ENDPOINT = "/oauth/v1/error";
}

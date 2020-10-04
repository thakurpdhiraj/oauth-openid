package com.dhitha.springbootoauthserver.oauth.dto;

import java.util.List;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/** @author Dhiraj */
@Getter
@ConfigurationProperties(prefix = "lms.oauth")
public class OpenIdConfigurationDTO {
  private final String issuer;
  private final String authorization_endpoint;
  private final String token_endpoint;
  private final String userinfo_endpoint;
  private final String jwks_uri;
  //  private final String registration_endpoint;
  private final List<String> token_endpoint_auth_methods_supported;
  private final List<String> token_endpoint_auth_signing_alg_values_supported;
  private final List<String> scopes_supported;
  private final List<String> response_types_supported;
  private final List<String> grant_types_supported;

  @ConstructorBinding
  public OpenIdConfigurationDTO(
      String issuer,
      String authorization_endpoint,
      String token_endpoint,
      String userinfo_endpoint,
      String jwks_uri,
      List<String> token_endpoint_auth_methods_supported,
      List<String> token_endpoint_auth_signing_alg_values_supported,
      List<String> scopes_supported,
      List<String> response_types_supported,
      List<String> grant_types_supported) {
    this.issuer = issuer;
    this.authorization_endpoint = authorization_endpoint;
    this.token_endpoint = token_endpoint;
    this.userinfo_endpoint = userinfo_endpoint;
    this.jwks_uri = jwks_uri;
    this.token_endpoint_auth_methods_supported = token_endpoint_auth_methods_supported;
    this.token_endpoint_auth_signing_alg_values_supported =
        token_endpoint_auth_signing_alg_values_supported;
    this.scopes_supported = scopes_supported;
    this.response_types_supported = response_types_supported;
    this.grant_types_supported = grant_types_supported;
  }
}

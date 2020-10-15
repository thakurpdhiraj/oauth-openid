package com.dhitha.springbootoauthserver.oauth.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/** @author Dhiraj */
@Getter
@AllArgsConstructor(onConstructor_={@ConstructorBinding})
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
}

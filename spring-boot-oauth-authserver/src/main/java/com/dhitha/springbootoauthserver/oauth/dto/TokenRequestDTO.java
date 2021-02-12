package com.dhitha.springbootoauthserver.oauth.dto;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for POST mapping for /auth/token/v1
 *
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
public class TokenRequestDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotEmpty(message = "Param 'grant_type' cannot be empty or null")
  private String grant_type;

  private String redirect_uri;

  private String code;

  private String refresh_token;

  private String client_id;

  private String client_secret;
}

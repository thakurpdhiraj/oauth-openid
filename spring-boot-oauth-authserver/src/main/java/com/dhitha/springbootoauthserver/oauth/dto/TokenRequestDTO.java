package com.dhitha.springbootoauthserver.oauth.dto;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for POST mapping for /auth/token/v1
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
public class TokenRequestDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotEmpty(message = "Param 'grant_type' cannot be empty or null")
  private String grant_type;

  @NotEmpty(message = "Param 'code' cannot be empty or null")
  private String code;

  @NotEmpty(message = "Param 'redirect_uri' cannot be empty or null")
  private String redirect_uri;

}

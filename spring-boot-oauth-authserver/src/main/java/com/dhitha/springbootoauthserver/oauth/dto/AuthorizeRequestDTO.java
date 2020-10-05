package com.dhitha.springbootoauthserver.oauth.dto;

import com.dhitha.springbootoauthserver.oauth.converter.RequiresSetToStringConversion;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query params dto for authorization request
 *
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorizeRequestDTO implements Serializable {
  private static final long serialVersionUID = 1;

  @NotEmpty(message = "'client_id' parameter must not be null or empty")
  private String client_id;

  @NotEmpty(message = "'redirect_uri' parameter must not be null or empty")
  private String redirect_uri;

  @NotEmpty(message = "'response_type' parameter must not be null or empty")
  private String response_type;

  @RequiresSetToStringConversion
  @NotEmpty(message = "'scope' parameter must not be null or empty")
  private Set<String> scope;

  private String state;

  private String nonce;

  private String access_type;
}

package com.dhitha.springbootoauthserver.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenErrorDTO {
  private String error;
  private String error_description;
}

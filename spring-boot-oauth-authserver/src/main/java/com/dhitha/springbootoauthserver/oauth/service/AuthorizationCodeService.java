package com.dhitha.springbootoauthserver.oauth.service;

import com.dhitha.springbootoauthserver.oauth.dto.AuthorizeRequestDTO;
import com.dhitha.springbootoauthserver.oauth.entity.AuthorizationCode;
import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.entity.User;
import com.dhitha.springbootoauthserver.oauth.error.notfound.OauthAuthCodeNotFoundException;
import java.util.Set;

/**
 * Service for {@link AuthorizationCode}
 *
 * @author Dhiraj
 */
public interface AuthorizationCodeService {

  /**
   * Find auth code details
   *
   * @param code authorization code
   * @return authorization code details if found
   * @throws OauthAuthCodeNotFoundException if no such code exists or if code has expired
   */
  AuthorizationCode findByCode(String code) throws OauthAuthCodeNotFoundException;

  /**
   * Generate a new authorization code
   *
   * @param code object containing user and client details for generating new auth code
   * @return generated authorization code details
   */
  AuthorizationCode save(AuthorizationCode code);

  /**
   * Generate a new authorization code
   *
   * @param requestDTO object containing auth request details
   * @param scopes requested scopes
   * @param client -
   * @param user -
   * @return generated authorization code details
   */
  AuthorizationCode save(
      AuthorizeRequestDTO requestDTO, Set<String> scopes, OauthClient client, User user);

  /**
   * Delete an authorization code details
   *
   * @param code code to delete
   */
  void delete(AuthorizationCode code);
}

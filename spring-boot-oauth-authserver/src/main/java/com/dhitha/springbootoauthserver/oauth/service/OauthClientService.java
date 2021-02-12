package com.dhitha.springbootoauthserver.oauth.service;

import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.error.notfound.OauthClientNotFoundException;

/**
 * Service for entity {@link OauthClient}
 *
 * @author Dhiraj
 */
public interface OauthClientService {

  /**
   * Find registered client details
   *
   * @param clientId unique if of registered client
   * @return client details if found
   * @throws OauthClientNotFoundException if no client exists with input id
   */
  OauthClient findByClientId(String clientId) throws OauthClientNotFoundException;

  /**
   * Validate the client credentials
   *
   * @param clientId unique client id generated on client registration
   * @param clientSecret client secret generated during client registration
   * @throws OauthClientNotFoundException if credentials are incorrect / client is not found
   */
  void validateClientCredentials(String clientId, String clientSecret)
      throws OauthClientNotFoundException;

  /**
   * Validate the client credentials. This method is used to determine client when authentication
   * used is basic. Internally uses {@link #validateClientCredentials(String, String)}
   *
   * @param authHeader basic encoded client credentials starting with {@literal 'Basic'}
   * @throws OauthClientNotFoundException if credentials are incorrect / client is not found
   */
  void validateClientCredentials(String authHeader) throws OauthClientNotFoundException;
}

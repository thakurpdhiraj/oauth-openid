package com.dhitha.springbootoauthserver.oauth.service;

import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.error.notfound.OauthClientNotFoundException;

/**
 * Service for entity {@link OauthClient}
 *
 * @author Dhiraj
 */
public interface OauthClientService {

  OauthClient findByClientId(String clientId) throws OauthClientNotFoundException;

  void validateClientCredentials(String clientId, String clientSecret)
      throws OauthClientNotFoundException;

  void validateClientCredentials(String authHeader) throws OauthClientNotFoundException;
}

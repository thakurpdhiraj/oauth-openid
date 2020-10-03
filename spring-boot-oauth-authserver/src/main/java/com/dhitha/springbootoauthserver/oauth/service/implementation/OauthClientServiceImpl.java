package com.dhitha.springbootoauthserver.oauth.service.implementation;

import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.error.notfound.OauthClientNotFoundException;
import com.dhitha.springbootoauthserver.oauth.repository.OauthClientRepository;
import com.dhitha.springbootoauthserver.oauth.service.OauthClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class to handle {@link OauthClient}
 *
 * @author Dhiraj
 */
@Service
public class OauthClientServiceImpl implements OauthClientService {

  @Autowired OauthClientRepository oauthClientRepository;

  @Override
  public OauthClient findByClientId(String clientId) throws OauthClientNotFoundException {
    return oauthClientRepository
        .findByClientId(clientId)
        .orElseThrow(
            () ->
                new OauthClientNotFoundException(
                    "Client " + clientId + " is not registered with Library Management System"));
  }

  @Override
  public OauthClient validateClientCredentials(String clientId, String clientSecret)
      throws OauthClientNotFoundException {
    return oauthClientRepository
        .findByClientId(clientId)
        .filter(client -> client.getClientSecret().equals(clientSecret))
        .orElseThrow(() -> new OauthClientNotFoundException("Invalid client_id / client_secret"));
  }
}

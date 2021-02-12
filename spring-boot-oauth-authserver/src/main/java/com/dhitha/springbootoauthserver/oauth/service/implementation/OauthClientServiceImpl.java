package com.dhitha.springbootoauthserver.oauth.service.implementation;

import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.error.notfound.OauthClientNotFoundException;
import com.dhitha.springbootoauthserver.oauth.repository.OauthClientRepository;
import com.dhitha.springbootoauthserver.oauth.service.OauthClientService;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implementation for {@link OauthClientService}
 *
 * @author Dhiraj
 */
@Service
@RequiredArgsConstructor
public class OauthClientServiceImpl implements OauthClientService {

  private final OauthClientRepository oauthClientRepository;

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
  public void validateClientCredentials(String clientId, String clientSecret)
      throws OauthClientNotFoundException {
    oauthClientRepository
        .findByClientId(clientId)
        .filter(client -> client.getClientSecret().equals(clientSecret))
        .orElseThrow(() -> new OauthClientNotFoundException("Invalid client_id / client_secret"));
  }

  @Override
  public void validateClientCredentials(String authHeader) throws OauthClientNotFoundException {
    String encodedCredentials = null;
    try {
      Assert.hasLength(authHeader, "'Basic Authorization' header must not be empty");
      if (authHeader.startsWith("Basic ")) {
        encodedCredentials = authHeader.substring(6);
      }
      Assert.hasLength(encodedCredentials, "Invalid 'Basic Authorization' header");
      String[] credentials = new String(Base64.getDecoder().decode(encodedCredentials)).split(":");
      if (credentials.length != 2) {
        throw new IllegalArgumentException("Invalid client_id / client_secret");
      }
      this.validateClientCredentials(credentials[0], credentials[1]);
    } catch (IllegalArgumentException e) {
      throw new OauthClientNotFoundException(e.getMessage());
    }
  }
}

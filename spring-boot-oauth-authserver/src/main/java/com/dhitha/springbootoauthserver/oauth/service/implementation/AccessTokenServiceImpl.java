package com.dhitha.springbootoauthserver.oauth.service.implementation;

import com.dhitha.springbootoauthserver.oauth.entity.AccessToken;
import com.dhitha.springbootoauthserver.oauth.entity.AuthorizationCode;
import com.dhitha.springbootoauthserver.oauth.error.notfound.AccessTokenNotFoundException;
import com.dhitha.springbootoauthserver.oauth.repository.AccessTokenRepository;
import com.dhitha.springbootoauthserver.oauth.service.AccessTokenService;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** @author Dhiraj */
@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

  private final AccessTokenRepository accessTokenRepository;

  @Override
  public AccessToken getToken(String token) throws AccessTokenNotFoundException {
    return accessTokenRepository
        .findByToken(token)
        .filter(accessToken -> accessToken.getAccessTokenExpiry().isAfter(LocalDateTime.now()))
        .orElseThrow(() -> new AccessTokenNotFoundException("Access Token is invalid / expired"));
  }

  @Override
  public AccessToken saveToken(AuthorizationCode code) {
    SecureRandom secureRandom = new SecureRandom();
    AccessToken accessToken =
        AccessToken.builder()
            .token(String.valueOf(secureRandom.nextLong()))
            .client(code.getClient())
            .user(code.getUser())
            .accessTokenExpiry(LocalDateTime.now().plusMinutes(60))
            .approvedScopes(code.getApprovedScopes())
            .refreshToken(code.isRefreshRequired() ? String.valueOf(secureRandom.nextLong()) : null)
            .build();
    return accessTokenRepository.saveAndFlush(accessToken);
  }

  @Override
  public AccessToken updateToken(AccessToken token) {
    SecureRandom secureRandom = new SecureRandom();
    token.setToken(String.valueOf(secureRandom.nextLong()));
    token.setAccessTokenExpiry(LocalDateTime.now().plusMinutes(60));
    return accessTokenRepository.saveAndFlush(token);
  }

  @Override
  public AccessToken getTokenByRefreshToken(String refreshToken)
      throws AccessTokenNotFoundException {
    return accessTokenRepository
        .findByRefreshToken(refreshToken)
        .orElseThrow(() -> new AccessTokenNotFoundException("Invalid Refresh Token"));
  }
}

package com.dhitha.springbootoauthserver.oauth.service;

import com.dhitha.springbootoauthserver.oauth.entity.AccessToken;
import com.dhitha.springbootoauthserver.oauth.entity.AuthorizationCode;
import com.dhitha.springbootoauthserver.oauth.error.notfound.AccessTokenNotFoundException;

/**
 * Service for access tokens
 *
 * @author Dhiraj
 */
public interface AccessTokenService {

  /**
   * Get token details
   *
   * @param token - access token
   * @return token details
   * @throws AccessTokenNotFoundException if token is not found or expired
   */
  AccessToken getToken(String token) throws AccessTokenNotFoundException;

  /**
   * Save a new access token
   *
   * @param code - authorization code details containing user, client, scope information
   * @return newly generated access token details
   */
  AccessToken saveToken(AuthorizationCode code);

  /**
   * Update token details with newly generated access token. This should mostly be only called when
   * refreshing a token
   *
   * @param token details to update
   * @return updated access token
   */
  AccessToken updateToken(AccessToken token);

  /**
   * Find access token details using a previously generated refresh token
   *
   * @param refreshToken refresh token which was returned by previously generated access token call
   * @return access token details
   * @throws AccessTokenNotFoundException if refresh token is not found
   */
  AccessToken getTokenByRefreshToken(String refreshToken) throws AccessTokenNotFoundException;
}

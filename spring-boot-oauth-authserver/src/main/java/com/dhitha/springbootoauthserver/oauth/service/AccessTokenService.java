package com.dhitha.springbootoauthserver.oauth.service;

import com.dhitha.springbootoauthserver.oauth.entity.AccessToken;
import com.dhitha.springbootoauthserver.oauth.entity.AuthorizationCode;
import com.dhitha.springbootoauthserver.oauth.error.notfound.AccessTokenNotFoundException;

/**
 * @author Dhiraj
 */
public interface AccessTokenService {

  AccessToken getToken(String token) throws AccessTokenNotFoundException;

  AccessToken saveToken(AuthorizationCode code);

  AccessToken updateToken(AccessToken token);

  AccessToken getTokenByRefreshToken(String refreshToken) throws AccessTokenNotFoundException;
}

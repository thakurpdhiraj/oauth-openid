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
  AuthorizationCode findByCode(String code) throws OauthAuthCodeNotFoundException;

  AuthorizationCode save(AuthorizationCode code);

  AuthorizationCode save(AuthorizeRequestDTO requestDTO, Set<String> scopes, OauthClient client, User user);

  void delete(String code);
}

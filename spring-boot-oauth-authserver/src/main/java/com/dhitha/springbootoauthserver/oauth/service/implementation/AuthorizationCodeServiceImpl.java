package com.dhitha.springbootoauthserver.oauth.service.implementation;

import com.dhitha.springbootoauthserver.oauth.dto.AuthorizeRequestDTO;
import com.dhitha.springbootoauthserver.oauth.entity.AuthorizationCode;
import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.resource.entity.User;
import com.dhitha.springbootoauthserver.oauth.error.notfound.OauthAuthCodeNotFoundException;
import com.dhitha.springbootoauthserver.oauth.repository.AuthorizationCodeRepository;
import com.dhitha.springbootoauthserver.oauth.service.AuthorizationCodeService;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for {@link AuthorizationCode}
 *
 * @author Dhiraj
 */
@Service
public class AuthorizationCodeServiceImpl implements AuthorizationCodeService {

  private final AuthorizationCodeRepository authorizationCodeRepository;

  @Autowired
  public AuthorizationCodeServiceImpl(AuthorizationCodeRepository authorizationCodeRepository) {
    this.authorizationCodeRepository = authorizationCodeRepository;
  }

  @Override
  public AuthorizationCode findByCode(String code) throws OauthAuthCodeNotFoundException {
    return authorizationCodeRepository
        .findByCode(code)
        .filter(
            authorizationCode -> authorizationCode.getExpirationDate().isAfter(LocalDateTime.now()))
        .orElseThrow(
            () -> new OauthAuthCodeNotFoundException("Authorization code invalid or expired"));
  }

  @Override
  public AuthorizationCode save(AuthorizationCode code) {
    SecureRandom sr = new SecureRandom();
    code.setCode(String.valueOf(sr.nextLong()));
    return authorizationCodeRepository.saveAndFlush(code);
  }

  @Override
  public AuthorizationCode save(
      AuthorizeRequestDTO requestDTO, Set<String> scopes, OauthClient client, User user) {
    AuthorizationCode code =
        AuthorizationCode.builder()
            .approvedScopes(scopes)
            .user(user)
            .client(client)
            .redirectUri(requestDTO.getRedirect_uri())
            .nonce(requestDTO.getNonce())
            .isRefreshRequired(
                requestDTO.getAccess_type() != null
                    && requestDTO.getAccess_type().equals("offline"))
            .expirationDate(LocalDateTime.now().plusMinutes(2))
            .build();
    return this.save(code);
  }

  @Override
  public void delete(AuthorizationCode code) {
    authorizationCodeRepository.delete(code);
  }
}

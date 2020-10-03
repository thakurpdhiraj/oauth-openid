package com.dhitha.springbootoauthserver.oauth.util;

import com.dhitha.springbootoauthserver.oauth.constant.AllowedGrant;
import com.dhitha.springbootoauthserver.oauth.constant.AllowedScope;
import com.dhitha.springbootoauthserver.oauth.dto.TokenRequestDTO;
import com.dhitha.springbootoauthserver.oauth.entity.AccessToken;
import com.dhitha.springbootoauthserver.oauth.entity.AuthorizationCode;
import com.dhitha.springbootoauthserver.oauth.entity.User;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericTokenException;
import com.dhitha.springbootoauthserver.oauth.error.notfound.OauthAuthCodeNotFoundException;
import com.dhitha.springbootoauthserver.oauth.error.notfound.OauthClientNotFoundException;
import com.dhitha.springbootoauthserver.oauth.service.AccessTokenService;
import com.dhitha.springbootoauthserver.oauth.service.AuthorizationCodeService;
import com.dhitha.springbootoauthserver.oauth.service.OauthClientService;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Utility for TokenController
 *
 * @author Dhiraj
 */
@Component
@Log4j2
public class TokenUtil {

  @Autowired AuthorizationCodeService authorizationCodeService;
  @Autowired OauthClientService oauthClientService;
  @Autowired AccessTokenService accessTokenService;
  @Autowired JWTUtil jwtUtil;

  private String[] validateAndExtractCredentials(String authHeader) throws GenericTokenException {
    String encodedCredentials;
    if (authHeader.isBlank()
        || !authHeader.startsWith("Basic ")
        || (encodedCredentials = authHeader.substring(6)).isBlank()) {
      throw new GenericTokenException(
          "invalid_auth_header",
          "'Basic Authorization' header must not be empty",
          HttpStatus.BAD_REQUEST);
    }
    return new String(Base64.getDecoder().decode(encodedCredentials)).split(":");
  }

  private void validateClientCredentials(String[] credentials) throws GenericTokenException {
    if (credentials.length != 2) {
      throw new GenericTokenException(
          "invalid_client", "Invalid client_id / client_secret", HttpStatus.BAD_REQUEST);
    }
    try {
      oauthClientService.validateClientCredentials(credentials[0], credentials[1]);
    } catch (OauthClientNotFoundException e) {
      throw new GenericTokenException("invalid_client", e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  private AuthorizationCode getAuthCode(String code) throws GenericTokenException {
    try {
      return authorizationCodeService.findByCode(code);
    } catch (OauthAuthCodeNotFoundException e) {
      throw new GenericTokenException("invalid_auth_code", e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  private void validateRedirectURI(String dbURI, String paramURI) throws GenericTokenException {
    if (!dbURI.equals(paramURI))
      throw new GenericTokenException(
          "invalid_redirect_uri", "the 'redirect_uri' is invalid", HttpStatus.BAD_REQUEST);
  }

  private void validateGrant(String grant) throws GenericTokenException {
    if (!AllowedGrant.contains(grant)) {
      throw new GenericTokenException(
          "unsupported_grant_type",
          "the 'grant_type' is invalid. Allowed grant_type values : "
              + AllowedGrant.getAllAsString(),
          HttpStatus.BAD_REQUEST);
    }
  }

  private AccessToken generateAccessToken(AuthorizationCode code) {
    AccessToken accessToken = accessTokenService.saveToken(code);
    authorizationCodeService.delete(code.getCode());
    return accessToken;
  }

  private String generateIdToken(String nonce, AccessToken token) throws GenericTokenException {
    Instant updatedInstant = token.getUpdatedAt().toInstant(ZoneOffset.UTC);
    Instant expiryInstant = token.getAccessTokenExpiry().toInstant(ZoneOffset.UTC);
    User user = token.getUser();
    Builder jwtBuilder =
        new Builder()
            .jwtID(token.getToken())
            .audience(token.getClient().getClientId())
            .issuer("http://localhost:8081")
            .expirationTime(Date.from(expiryInstant))
            .issueTime(Date.from(updatedInstant))
            .notBeforeTime(Date.from(updatedInstant))
            .subject(user.getId().toString())
            .claim("nonce", nonce)
            .claim("scope", String.join(",", token.getApprovedScopes()));
    if (token.getApprovedScopes().contains(AllowedScope.PROFILE.getValue())) {
      jwtBuilder.claim("name", user.getName());
    }
    return jwtUtil.signAndSerializeJWT(jwtBuilder.build());
  }

  public JSONObject generateTokenResponse(String authHeader, TokenRequestDTO tokenRequestDTO)
      throws GenericTokenException {
    // validate client credentials
    String[] credentials = this.validateAndExtractCredentials(authHeader);
    this.validateClientCredentials(credentials);
    // validate code
    AuthorizationCode code = this.getAuthCode(tokenRequestDTO.getCode());
    this.validateRedirectURI(code.getRedirectUri(), tokenRequestDTO.getRedirect_uri());
    this.validateGrant(tokenRequestDTO.getGrant_type());
    String nonce = code.getNonce();
    AccessToken accessToken = this.generateAccessToken(code);
    String idToken = this.generateIdToken(nonce, accessToken);
    JSONObject tokenResponse = new JSONObject();
    tokenResponse.appendField("access_token", accessToken.getToken());
    if (accessToken.getRefreshToken() != null) {
      tokenResponse.appendField("refresh_token", accessToken.getRefreshToken());
    }
    tokenResponse.appendField("token_type", "Bearer");
    tokenResponse.appendField("expires_in", 3600L);
    tokenResponse.appendField("id_token", idToken);
    tokenResponse.appendField("scope", String.join(",", accessToken.getApprovedScopes()));
    return tokenResponse;
  }
}

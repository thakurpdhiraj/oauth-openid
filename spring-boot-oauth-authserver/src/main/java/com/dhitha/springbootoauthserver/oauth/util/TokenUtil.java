package com.dhitha.springbootoauthserver.oauth.util;

import com.dhitha.springbootoauthserver.oauth.constant.AllowedGrant;
import com.dhitha.springbootoauthserver.oauth.constant.AllowedScope;
import com.dhitha.springbootoauthserver.oauth.dto.TokenRequestDTO;
import com.dhitha.springbootoauthserver.oauth.entity.AccessToken;
import com.dhitha.springbootoauthserver.oauth.entity.AuthorizationCode;
import com.dhitha.springbootoauthserver.resource.entity.User;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericAPIException;
import com.dhitha.springbootoauthserver.oauth.error.notfound.AccessTokenNotFoundException;
import com.dhitha.springbootoauthserver.oauth.error.notfound.OauthAuthCodeNotFoundException;
import com.dhitha.springbootoauthserver.oauth.error.notfound.OauthClientNotFoundException;
import com.dhitha.springbootoauthserver.oauth.service.AccessTokenService;
import com.dhitha.springbootoauthserver.oauth.service.AuthorizationCodeService;
import com.dhitha.springbootoauthserver.oauth.service.OauthClientService;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Utility for TokenController
 *
 * @author Dhiraj
 */
@Component
@Log4j2
public class TokenUtil {

  private final OauthClientService oauthClientService;
  private final AuthorizationCodeService authorizationCodeService;
  private final AccessTokenService accessTokenService;
  private final JWTUtil jwtUtil;

  @Autowired
  public TokenUtil(
      OauthClientService oauthClientService,
      AuthorizationCodeService authorizationCodeService,
      AccessTokenService accessTokenService,
      JWTUtil jwtUtil) {
    this.oauthClientService = oauthClientService;
    this.authorizationCodeService = authorizationCodeService;
    this.accessTokenService = accessTokenService;
    this.jwtUtil = jwtUtil;
  }

  public void validateClientCredentials(String authHeader, TokenRequestDTO tokenRequestDTO)
      throws GenericAPIException {
    try {
      if (StringUtils.isEmpty(authHeader)) {
        Assert.notNull(tokenRequestDTO.getClient_id(), "Missing client_id parameter");
        Assert.notNull(tokenRequestDTO.getClient_secret(), "Missing client_secret parameter");
        oauthClientService.validateClientCredentials(
            tokenRequestDTO.getClient_id(), tokenRequestDTO.getClient_secret());
      } else {
        oauthClientService.validateClientCredentials(authHeader);
      }
    } catch (IllegalArgumentException e) {
      throw new GenericAPIException("invalid_request", e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (OauthClientNotFoundException e) {
      throw new GenericAPIException(
          "invalid_client", "Invalid Credentials", HttpStatus.UNAUTHORIZED);
    }
  }

  public AllowedGrant validateAndFetchGrant(String grant) throws GenericAPIException {
    try {
      return AllowedGrant.get(grant);
    } catch (IllegalArgumentException e) {
      throw new GenericAPIException(
          "unsupported_grant_type", e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  public JSONObject createAccessTokenForAuthCodeFlow(TokenRequestDTO tokenRequestDTO)
      throws GenericAPIException {
    AuthorizationCode code = this.getAuthCode(tokenRequestDTO.getCode());
    this.validateRedirectURI(code.getRedirectUri(), tokenRequestDTO.getRedirect_uri());
    String nonce = code.getNonce();
    AccessToken accessToken = this.generateAccessToken(code);
    JSONObject tokenResponse = new JSONObject();
    tokenResponse.appendField("access_token", accessToken.getToken());
    tokenResponse.appendField("token_type", "Bearer");
    tokenResponse.appendField("expires_in", 3600L);
    tokenResponse.appendField("scope", accessToken.getApprovedScopes());
    if (accessToken.getRefreshToken() != null) {
      tokenResponse.appendField("refresh_token", accessToken.getRefreshToken());
    }
    if (accessToken.getApprovedScopes().contains(AllowedScope.OPENID.getValue())) {
      String idToken = this.generateIdToken(nonce, accessToken);
      tokenResponse.appendField("id_token", idToken);
    }
    return tokenResponse;
  }

  private AuthorizationCode getAuthCode(String code) throws GenericAPIException {
    try {
      Assert.notNull(code, "param 'code' cannot be null / empty");
      return authorizationCodeService.findByCode(code);
    } catch (IllegalArgumentException e) {
      throw new GenericAPIException("invalid_request", e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (OauthAuthCodeNotFoundException e) {
      throw new GenericAPIException("invalid_grant", e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  private void validateRedirectURI(String dbURI, String paramURI) throws GenericAPIException {
    if (!dbURI.equals(paramURI))
      throw new GenericAPIException(
          "invalid_grant", "the 'redirect_uri' is invalid", HttpStatus.UNAUTHORIZED);
  }

  private AccessToken generateAccessToken(AuthorizationCode code) {
    AccessToken accessToken = accessTokenService.saveToken(code);
    authorizationCodeService.delete(code);
    return accessToken;
  }

  private String generateIdToken(String nonce, AccessToken token) throws GenericAPIException {
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
            .claim("scope", String.join(", ", token.getApprovedScopes()));
    if (token.getApprovedScopes().contains(AllowedScope.PROFILE.getValue())) {
      jwtBuilder.claim("name", user.getName()).claim("email", user.getEmail());
    }
    return jwtUtil.signAndSerializeJWT(jwtBuilder.build());
  }

  public JSONObject createAccessTokenForRefreshTokenFlow(TokenRequestDTO tokenRequestDTO)
      throws GenericAPIException {
    AccessToken updateToken =
        accessTokenService.updateToken(
            this.getTokenByRefreshToken(tokenRequestDTO.getRefresh_token()));
    JSONObject tokenResponse = new JSONObject();
    tokenResponse.appendField("access_token", updateToken.getToken());
    tokenResponse.appendField("token_type", "Bearer");
    tokenResponse.appendField("expires_in", 3600L);
    tokenResponse.appendField("scope", updateToken.getApprovedScopes());

    return tokenResponse;
  }

  private AccessToken getTokenByRefreshToken(String refreshToken) throws GenericAPIException {
    try {
      Assert.notNull(
          refreshToken,
          "Param 'refresh_token' cannot be null / empty for grant_type=refresh_token");
      return accessTokenService.getTokenByRefreshToken(refreshToken);
    } catch (IllegalArgumentException e) {
      throw new GenericAPIException("invalid_request", e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (AccessTokenNotFoundException e) {
      throw new GenericAPIException("invalid_grant", e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}

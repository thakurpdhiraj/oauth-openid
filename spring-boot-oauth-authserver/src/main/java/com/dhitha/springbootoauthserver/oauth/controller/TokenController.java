package com.dhitha.springbootoauthserver.oauth.controller;

import com.dhitha.springbootoauthserver.oauth.constant.AllowedGrant;
import com.dhitha.springbootoauthserver.oauth.constant.Endpoints;
import com.dhitha.springbootoauthserver.oauth.dto.TokenRequestDTO;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericAPIException;
import com.dhitha.springbootoauthserver.oauth.util.TokenUtil;
import javax.validation.Valid;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint to fetch access & refresh token
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping(Endpoints.TOKEN_ENDPOINT)
public class TokenController {

  private final TokenUtil tokenUtil;

  @Autowired
  public TokenController(TokenUtil tokenUtil) {
    this.tokenUtil = tokenUtil;
  }

  // Basic YXBwLmxtcy4xOnk4WDJZaW8wNWtIaVRzaXNZRTY4
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<?> getAccessToken(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
      @Valid TokenRequestDTO tokenRequestDTO,
      BindingResult bindingResult)
      throws GenericAPIException {
    if (bindingResult.hasErrors()) {
      throw new GenericAPIException(bindingResult);
    }
    tokenUtil.validateClientCredentials(authHeader, tokenRequestDTO);

    AllowedGrant grant = tokenUtil.validateAndFetchGrant(tokenRequestDTO.getGrant_type());

    JSONObject tokenResponseDTO;
    switch (grant) {
      case AUTHORIZATION_CODE:
        tokenResponseDTO = tokenUtil.createAccessTokenForAuthCodeFlow(tokenRequestDTO);
        break;
      case REFRESH_TOKEN:
        tokenResponseDTO = tokenUtil.createAccessTokenForRefreshTokenFlow(tokenRequestDTO);
        break;
      default:
        throw new GenericAPIException(
            "unsupported_grant_type", "Unexpected value: " + grant, HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(tokenResponseDTO);
  }
}

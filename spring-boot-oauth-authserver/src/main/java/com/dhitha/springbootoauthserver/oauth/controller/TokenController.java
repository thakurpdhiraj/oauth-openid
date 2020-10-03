package com.dhitha.springbootoauthserver.oauth.controller;

import com.dhitha.springbootoauthserver.oauth.constant.AllowedGrant;
import com.dhitha.springbootoauthserver.oauth.dto.TokenRequestDTO;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericTokenException;
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
@RequestMapping("oauth/token/v1")
public class TokenController {

  @Autowired TokenUtil tokenUtil;

  // Basic YXBwLmxtcy4xOnk4WDJZaW8wNWtIaVRzaXNZRTY4
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<?> getAccessToken(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
      @Valid TokenRequestDTO tokenRequestDTO,
      BindingResult bindingResult)
      throws GenericTokenException {
    if (bindingResult.hasErrors()) {
      throw new GenericTokenException(bindingResult);
    }
    tokenUtil.validateClientCredentials(authHeader);

    AllowedGrant grant = tokenUtil.validateAndFetchGrant(tokenRequestDTO.getGrant_type());

    JSONObject tokenResponseDTO;
    switch (grant){
      case AUTHORIZATION_CODE:
        tokenResponseDTO =  tokenUtil.createAccessTokenForAuthCodeFlow(tokenRequestDTO);
        break;
      case REFRESH_TOKEN:
        tokenResponseDTO =  tokenUtil.createAccessTokenForRefreshTokenFlow(tokenRequestDTO);
        break;
      default:
        throw new GenericTokenException("invalid_grant","Unexpected value: " + grant, HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(tokenResponseDTO);
  }
}

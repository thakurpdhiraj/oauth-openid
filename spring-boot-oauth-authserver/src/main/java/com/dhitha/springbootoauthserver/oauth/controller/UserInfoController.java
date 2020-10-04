package com.dhitha.springbootoauthserver.oauth.controller;

import com.dhitha.springbootoauthserver.oauth.constant.AllowedScope;
import com.dhitha.springbootoauthserver.oauth.constant.Endpoints;
import com.dhitha.springbootoauthserver.oauth.entity.AccessToken;
import com.dhitha.springbootoauthserver.oauth.entity.User;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericAPIException;
import com.dhitha.springbootoauthserver.oauth.error.notfound.AccessTokenNotFoundException;
import com.dhitha.springbootoauthserver.oauth.service.AccessTokenService;
import com.dhitha.springbootoauthserver.oauth.service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author Dhiraj */
@RestController
@RequestMapping(Endpoints.USERINFO_ENDPOINT)
public class UserInfoController {

  @Autowired private UserService userService;

  @Autowired private AccessTokenService accessTokenService;

  @GetMapping
  public ResponseEntity<?> getUserInfo(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) throws GenericAPIException {
    if (!authHeader.startsWith("Bearer ")) {
      throw new GenericAPIException(
          "invalid_request", "Invalid credentials", HttpStatus.BAD_REQUEST);
    }
    String accessToken = authHeader.substring(7);
    try {
      AccessToken token = accessTokenService.getToken(accessToken);
      User user = token.getUser();
      JSONObject userJson = new JSONObject()
          .appendField("sub", user.getId());
      if(token.getApprovedScopes().contains(AllowedScope.PROFILE.getValue())){
          userJson.appendField("name", user.getName())
              .appendField("email", user.getEmail());
      }
      return ResponseEntity.ok(userJson);
    } catch (AccessTokenNotFoundException e) {
      throw new GenericAPIException(
          "invalid_request", "Invalid credentials", HttpStatus.BAD_REQUEST);
    }
  }
}

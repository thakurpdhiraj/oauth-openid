package com.dhitha.springbootoauthserver.oauth.controller;

import static com.dhitha.springbootoauthserver.oauth.constant.Endpoints.CERTS_ENDPOINT;
import static com.dhitha.springbootoauthserver.oauth.constant.Endpoints.WELL_KNOWN_ENDPOINT;

import com.dhitha.springbootoauthserver.oauth.dto.OpenIdConfigurationDTO;
import com.dhitha.springbootoauthserver.oauth.error.generic.GenericTokenException;
import com.dhitha.springbootoauthserver.oauth.util.JWTUtil;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import java.util.Collections;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OpenId Connect Meta Data Discovery endpoints
 *
 * @author Dhiraj
 */
@RestController
public class OauthMetaDataController {

  @Autowired JWTUtil jwtUtil;
  @Autowired OpenIdConfigurationDTO configurationDTO;

  @GetMapping(value = WELL_KNOWN_ENDPOINT)
  public ResponseEntity<?> getOpenIdConfiguration() {
    return ResponseEntity.ok(configurationDTO);
  }

  @GetMapping(value = CERTS_ENDPOINT)
  public ResponseEntity<?> getJWK() {
    try {
      RSAKey publicKey = jwtUtil.getPublicKey();
      JSONObject jwkJson =
          publicKey
              .toJSONObject()
              .appendField("use", KeyUse.SIGNATURE.getValue())
              .appendField("kid", "1");
      JSONObject keys = new JSONObject();
      keys.put("keys", Collections.singletonList(jwkJson));
      return ResponseEntity.ok(keys);
    } catch (GenericTokenException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new JSONObject()
                  .appendField("error", "server_error")
                  .appendField("error_description", "Something went wrong"));
    }
  }
}

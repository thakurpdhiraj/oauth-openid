package com.dhitha.springbootoauthserver.oauth.util;

import com.dhitha.springbootoauthserver.oauth.error.generic.GenericAPIException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.IOUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/** @author Dhiraj */
@Component
@Slf4j
@RequiredArgsConstructor
public class JWTUtil {
  private final ResourceLoader resourceLoader;

  public RSAKey getPublicKey() throws GenericAPIException {
    Resource resource = resourceLoader.getResource("classpath:/certs/lms-public-key.pem");
    try {
      String keyString = IOUtils.readFileToString(resource.getFile());
      JWK jwk = JWK.parseFromPEMEncodedObjects(keyString);
      return jwk.toRSAKey();
    } catch (IOException | JOSEException e) {
      log.error("Error fetching public key ", e);
      throw new GenericAPIException();
    }
  }

  public RSAKey getPrivateKey() throws GenericAPIException {
    Resource resource = resourceLoader.getResource("classpath:/certs/lms-private-key.pem");
    try {
      String keyString = IOUtils.readFileToString(resource.getFile());
      JWK jwk = JWK.parseFromPEMEncodedObjects(keyString);
      return jwk.toRSAKey();
    } catch (IOException | JOSEException e) {
      log.error("Error fetching public key ", e);
      throw new GenericAPIException();
    }
  }

  public String signAndSerializeJWT(JWTClaimsSet claimsSet) throws GenericAPIException {
    try {
      JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build();
      RSASSASigner rsassaSigner = new RSASSASigner(getPrivateKey());
      SignedJWT signedJWT = new SignedJWT(header, claimsSet);
      signedJWT.sign(rsassaSigner);
      return signedJWT.serialize();
    } catch (JOSEException e) {
      log.error("Error signing jwt using private key ", e);
      throw new GenericAPIException();
    }
  }

  public boolean verifySignedJWT(String token) throws GenericAPIException {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      JWSVerifier verifier = new RSASSAVerifier(getPublicKey());
      return signedJWT.verify(verifier);
    } catch (ParseException | JOSEException e) {
      log.error("Error verifying jwt using public key ", e);
      throw new GenericAPIException(
          "invalid_token", "Token invalid / Expired", HttpStatus.BAD_REQUEST);
    }
  }

  public boolean verifyJWT(String token) throws GenericAPIException {
    try {
      DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
      JWKSet jwkSet = new JWKSet(getPublicKey());
      JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);
      JWSKeySelector<SecurityContext> verificationKeySelector =
          new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);
      jwtProcessor.setJWSKeySelector(verificationKeySelector);
      jwtProcessor.setJWTClaimsSetVerifier(
          new DefaultJWTClaimsVerifier<>(
              new JWTClaimsSet.Builder().issuer("http://localhost:8081").build(),
              new HashSet<>(Collections.singletonList("exp"))));
      jwtProcessor.process(token, null);
      return true;
    } catch (IllegalStateException | ParseException | JOSEException | BadJOSEException e) {
      log.error("Error verifying jwt using public key ", e);
      throw new GenericAPIException(
          "invalid_token", "Token invalid / Expired", HttpStatus.BAD_REQUEST);
    }
  }
}

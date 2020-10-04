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
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.IOUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.io.IOException;
import java.text.ParseException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/** @author Dhiraj */
@Component
@Log4j2
public class JWTUtil {
  private final ResourceLoader resourceLoader;

  @Autowired
  public JWTUtil(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

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
      throw new GenericAPIException();
    }
  }
}

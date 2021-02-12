package com.dhitha.springbootoauthserver.oauth.repository;

import com.dhitha.springbootoauthserver.oauth.entity.AuthorizationCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for entity {@link AuthorizationCode}
 *
 * @author Dhiraj
 */
@Repository
public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode, Long> {

  /**
   * Find authorization code details by auth code
   *
   * @param code authorization code
   * @return authorization code details or empty optional
   */
  Optional<AuthorizationCode> findByCode(String code);
}

package com.dhitha.springbootoauthserver.oauth.repository;

import com.dhitha.springbootoauthserver.oauth.entity.AuthorizationCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link AuthorizationCode}
 * @author Dhiraj
 */
@Repository
public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode,Long> {
  Optional<AuthorizationCode> findByCode(String code);
}

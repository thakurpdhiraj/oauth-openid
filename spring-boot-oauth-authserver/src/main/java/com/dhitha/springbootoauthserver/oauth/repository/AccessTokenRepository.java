package com.dhitha.springbootoauthserver.oauth.repository;

import com.dhitha.springbootoauthserver.oauth.entity.AccessToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dhiraj
 */
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {
    Optional<AccessToken> findByRefreshToken(String refreshToken);
}

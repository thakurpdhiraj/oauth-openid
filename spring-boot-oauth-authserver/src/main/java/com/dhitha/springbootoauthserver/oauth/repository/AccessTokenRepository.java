package com.dhitha.springbootoauthserver.oauth.repository;

import com.dhitha.springbootoauthserver.oauth.entity.AccessToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for entity {@link AccessToken}
 *
 * @author Dhiraj
 */
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

  /**
   * Find token details by refresh token
   *
   * @param refreshToken -
   * @return access token details or empty optional
   */
  Optional<AccessToken> findByRefreshToken(String refreshToken);

  /**
   * Find token details by access token
   *
   * @param token access token
   * @return access token details or empty optional
   */
  Optional<AccessToken> findByToken(String token);
}

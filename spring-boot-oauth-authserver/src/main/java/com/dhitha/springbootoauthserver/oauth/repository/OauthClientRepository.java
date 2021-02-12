package com.dhitha.springbootoauthserver.oauth.repository;

import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for entity {@link OauthClient}
 *
 * @author Dhiraj
 */
@Repository
public interface OauthClientRepository extends JpaRepository<OauthClient, Long> {

  /**
   * Find client details by client id
   *
   * @param clientId unique client id
   * @return client details or empty optional
   */
  Optional<OauthClient> findByClientId(String clientId);
}

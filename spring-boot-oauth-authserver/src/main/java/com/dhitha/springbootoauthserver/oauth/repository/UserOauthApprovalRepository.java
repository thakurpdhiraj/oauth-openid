package com.dhitha.springbootoauthserver.oauth.repository;

import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.entity.User;
import com.dhitha.springbootoauthserver.oauth.entity.UserOauthApproval;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for entity {@link UserOauthApproval}
 *
 * @author Dhiraj
 */
@Repository
public interface UserOauthApprovalRepository extends JpaRepository<UserOauthApproval, Long> {

  /**
   * Find all oauth approvals of a user for a client
   *
   * @param user user which registered for client
   * @param client client for which approvals were given
   * @return user's auth approval details ofr empty optional
   */
  Optional<UserOauthApproval> findByUserAndClient(User user, OauthClient client);

  /**
   * Delete all oauth approval of a user for a client
   *
   * @param user user which registered for client
   * @param client client for which approvals were given
   * @return returns 1 if deletion was successful
   */
  Long deleteByUserAndClient(User user, OauthClient client);
}

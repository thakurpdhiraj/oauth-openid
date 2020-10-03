package com.dhitha.springbootoauthserver.oauth.repository;

import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.entity.User;
import com.dhitha.springbootoauthserver.oauth.entity.UserOauthApproval;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** @author Dhiraj */
@Repository
public interface UserOauthApprovalRepository extends JpaRepository<UserOauthApproval, Long> {
  Optional<UserOauthApproval> findByUserAndClient(User user, OauthClient client);

  Long deleteByUserAndClient(User user, OauthClient client);
}

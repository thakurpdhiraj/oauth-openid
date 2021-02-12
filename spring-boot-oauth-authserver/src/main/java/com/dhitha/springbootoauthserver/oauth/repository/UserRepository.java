package com.dhitha.springbootoauthserver.oauth.repository;

import com.dhitha.springbootoauthserver.oauth.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for entity {@link User}
 *
 * @author Dhiraj
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Find a user details by username
   * @param username username of user to find
   * @return user or empty optional
   */
  Optional<User> findByUsername(String username);
}

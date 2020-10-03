package com.dhitha.springbootoauthserver.oauth.repository;

import com.dhitha.springbootoauthserver.oauth.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link User}
 *
 * @author Dhiraj
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
  Optional<User> findByUsername(String username);
}

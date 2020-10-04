package com.dhitha.springbootoauthserver.oauth.service;

import com.dhitha.springbootoauthserver.oauth.error.notfound.UserNotFoundException;
import com.dhitha.springbootoauthserver.oauth.entity.User;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Service for entity {@link User} which also extends {@link UserDetailsService}
 *
 * @author Dhiraj
 */
public interface UserService extends UserDetailsService {
  User findById(Long id) throws UserNotFoundException;
}

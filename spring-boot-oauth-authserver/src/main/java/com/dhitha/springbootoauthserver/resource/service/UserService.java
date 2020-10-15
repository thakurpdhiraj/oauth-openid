package com.dhitha.springbootoauthserver.resource.service;

import com.dhitha.springbootoauthserver.oauth.error.notfound.UserNotFoundException;
import com.dhitha.springbootoauthserver.resource.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Service for entity {@link User} which also extends {@link UserDetailsService}
 *
 * @author Dhiraj
 */
public interface UserService extends UserDetailsService {
  User findById(Long id) throws UserNotFoundException;
}

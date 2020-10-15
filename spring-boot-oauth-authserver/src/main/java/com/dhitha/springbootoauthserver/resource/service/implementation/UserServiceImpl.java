package com.dhitha.springbootoauthserver.resource.service.implementation;

import com.dhitha.springbootoauthserver.resource.entity.User;
import com.dhitha.springbootoauthserver.oauth.error.notfound.UserNotFoundException;
import com.dhitha.springbootoauthserver.resource.repository.UserRepository;
import com.dhitha.springbootoauthserver.resource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation for service {@link UserService} with bean name {@code "userService"}
 *
 * @author Dhiraj
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found for username" + username));
  }

  @Override
  public User findById(Long id) throws UserNotFoundException {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
  }
}

package com.dhitha.springbootoauthserver.oauth.service.implementation;

import com.dhitha.springbootoauthserver.oauth.error.notfound.UserNotFoundException;
import com.dhitha.springbootoauthserver.oauth.entity.User;
import com.dhitha.springbootoauthserver.oauth.repository.UserRepository;
import com.dhitha.springbootoauthserver.oauth.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation for service {@link UserService} with bean name {@code "userService"}
 *
 * @author Dhiraj
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

  @Autowired UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found for username" + username));
  }

  @Override
  public User findByUsername(String username) throws UserNotFoundException {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("User not found with username " + username));
  }

  @Override
  public User findById(Long id) throws UserNotFoundException {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
  }

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public User save(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.saveAndFlush(user);
  }

  @Override
  public User update(User user) throws UserNotFoundException {
    this.findById(user.getId());
    return this.save(user);
  }

  @Override
  public void deleteById(Long id) throws UserNotFoundException{
    this.findById(id);
    userRepository.deleteById(id);
  }

  @Override
  public void delete(User user) throws UserNotFoundException{
    this.deleteById(user.getId());
  }
}

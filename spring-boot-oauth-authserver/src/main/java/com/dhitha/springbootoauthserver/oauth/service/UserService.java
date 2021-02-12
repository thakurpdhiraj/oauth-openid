package com.dhitha.springbootoauthserver.oauth.service;

import com.dhitha.springbootoauthserver.oauth.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Service for entity {@link User} which also extends {@link UserDetailsService}
 *
 * @author Dhiraj
 */
public interface UserService extends UserDetailsService {}

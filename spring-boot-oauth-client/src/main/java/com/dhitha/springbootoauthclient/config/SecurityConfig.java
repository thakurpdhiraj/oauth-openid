package com.dhitha.springbootoauthclient.config;

import java.util.HashSet;
import java.util.Set;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

/**
 * Configure oauth security
 *
 * @author Dhiraj
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .oauth2Login()
        .userInfoEndpoint()
        .oidcUserService(this.oidcUserService());
  }

  /**
   * Set custom roles received in the id token to the authenticated user session object
   *
   * @return OAuth2UserService
   */
  private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
    final OidcUserService userService = new OidcUserService();
    return (userRequest) -> {
      OidcUser oidcUser = userService.loadUser(userRequest);
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      String roles = (String) oidcUser.getAttributes().getOrDefault("role", "USER");
      for (String authority : roles.split(" ")) {
        mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + authority));
      }
      oidcUser =
          new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
      return oidcUser;
    };
  }
}

package com.dhitha.springbootoauthserver.oauth.config;

import com.dhitha.springbootoauthserver.oauth.filter.AuthorizeRequestDTOSetterFilter;
import com.dhitha.springbootoauthserver.oauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security config for mvc
 *
 * @author Dhiraj
 */
@Configuration
public class OauthSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  UserService userService;

  @Autowired
  AuthorizeRequestDTOSetterFilter authorizationReqParamSetterFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/oauth/token/v1")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .loginPage("/oauth/login/v1")
        .loginProcessingUrl("/validate_oauth")
        .failureUrl("/oauth/login/v1?error")
        .usernameParameter("username")
        .passwordParameter("password")
        .permitAll();
    http.addFilterBefore(authorizationReqParamSetterFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(this.passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

}

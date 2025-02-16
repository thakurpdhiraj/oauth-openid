package com.dhitha.springbootoauthserver.oauth.config;

import static com.dhitha.springbootoauthserver.oauth.constant.Endpoints.AUTHORIZATION_ENDPOINT;
import static com.dhitha.springbootoauthserver.oauth.constant.Endpoints.CERTS_ENDPOINT;
import static com.dhitha.springbootoauthserver.oauth.constant.Endpoints.LOGIN_ENDPOINT;
import static com.dhitha.springbootoauthserver.oauth.constant.Endpoints.TOKEN_ENDPOINT;
import static com.dhitha.springbootoauthserver.oauth.constant.Endpoints.USERINFO_ENDPOINT;
import static com.dhitha.springbootoauthserver.oauth.constant.Endpoints.WELL_KNOWN_ENDPOINT;

import com.dhitha.springbootoauthserver.oauth.filter.AuthorizeRequestDTOSetterFilter;
import com.dhitha.springbootoauthserver.oauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
@RequiredArgsConstructor
@Order(1)
public class OauthSecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService userService;

  private final AuthorizeRequestDTOSetterFilter authorizationReqParamSetterFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers(
            TOKEN_ENDPOINT, WELL_KNOWN_ENDPOINT, CERTS_ENDPOINT, USERINFO_ENDPOINT, "/h2-oauth")
        .permitAll()
        .antMatchers(AUTHORIZATION_ENDPOINT)
        .authenticated()
        .and()
        .formLogin()
        .loginPage(LOGIN_ENDPOINT)
        .loginProcessingUrl("/validate_oauth")
        .failureUrl(LOGIN_ENDPOINT + "?error")
        .usernameParameter("username")
        .passwordParameter("password")
        .permitAll();
    http.addFilterBefore(
        authorizationReqParamSetterFilter, UsernamePasswordAuthenticationFilter.class);
    http.headers().frameOptions().disable(); // Only enable for h2 development
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(this.passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}

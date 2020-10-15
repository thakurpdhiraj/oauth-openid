package com.dhitha.springbootoauthserver.resource.config;

import com.dhitha.springbootoauthserver.resource.filter.ResourceAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** @author Dhiraj */
@Configuration
@Order(2)
public class ResourceSecurityConfig extends WebSecurityConfigurerAdapter {

  private final ResourceAuthenticationFilter resourceAuthenticationFilter;

  @Autowired
  public ResourceSecurityConfig(ResourceAuthenticationFilter resourceAuthenticationFilter) {
    this.resourceAuthenticationFilter = resourceAuthenticationFilter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.antMatcher("/api/resources/**")
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .httpBasic()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(resourceAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .csrf()
        .disable();
  }
}

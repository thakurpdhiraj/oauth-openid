package com.dhitha.springbootoauthclient.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author Dhiraj */
@RestController
@RequestMapping("/")
public class ClientController {

  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> getAuthenticatedUser(Authentication principal) {
    return ResponseEntity.ok(principal.getPrincipal());
  }
}

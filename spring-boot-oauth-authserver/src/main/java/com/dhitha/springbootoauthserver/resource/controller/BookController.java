package com.dhitha.springbootoauthserver.resource.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dhiraj
 */
@RestController
@RequestMapping("/resources/books")
public class BookController {

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('read.book')")
  public ResponseEntity<?> getBook(@PathVariable("id") Long id){
    return ResponseEntity.ok("Hello; Here is your book "+id);
  }
}

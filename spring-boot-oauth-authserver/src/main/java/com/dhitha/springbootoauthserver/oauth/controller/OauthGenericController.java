package com.dhitha.springbootoauthserver.oauth.controller;

import com.dhitha.springbootoauthserver.oauth.constant.Endpoints;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Map the login controller for OAuth
 *
 * @author Dhiraj
 */
@Controller
@Log4j2
public class OauthGenericController {
  // http://localhost:8081/oauth/v1/authorize?client_id=app.lms.1&redirect_uri=http:%2F%2Flocalhost:8181%2Fwholesale&response_type=code&scope=openid&state=50111
  @GetMapping(Endpoints.LOGIN_ENDPOINT)
  public String redirectToLogin(){
    return "oauth_login";
  }

  @GetMapping(Endpoints.OAUTH_ERROR_ENDPOINT)
  public String redirectToError(@RequestParam("error") String error,
      @RequestParam("error_description") String error_description,
      Model model){
    model.addAttribute("error", error);
    model.addAttribute("error_description", error_description);
    return "oauth_error";
  }
}

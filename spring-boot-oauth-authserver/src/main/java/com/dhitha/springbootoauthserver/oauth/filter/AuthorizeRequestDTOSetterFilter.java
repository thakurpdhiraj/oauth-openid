package com.dhitha.springbootoauthserver.oauth.filter;

import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_CLIENT;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_REQ_PARAMS;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.SCOPE_TOKEN;

import com.dhitha.springbootoauthserver.oauth.constant.Endpoints;
import com.dhitha.springbootoauthserver.oauth.dto.AuthorizeRequestDTO;
import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.error.notfound.OauthClientNotFoundException;
import com.dhitha.springbootoauthserver.oauth.service.OauthClientService;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** @author Dhiraj */
@Component
public class AuthorizeRequestDTOSetterFilter extends OncePerRequestFilter {

  private final OauthClientService oauthClientService;

  @Autowired
  public AuthorizeRequestDTOSetterFilter(OauthClientService oauthClientService) {
    this.oauthClientService = oauthClientService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException{
    if (Endpoints.AUTHORIZATION_ENDPOINT.equals(request.getServletPath())) {
      String redirectURI = Endpoints.OAUTH_ERROR_ENDPOINT;
      if (request.getSession().getAttribute(AUTH_REQ_ATTRIBUTE_REQ_PARAMS) == null) {
        String[] client_ids = request.getParameterValues("client_id");
        if(client_ids == null || client_ids.length != 1){
          redirectToError(response, "Required param 'client_id' missing",redirectURI);
          return;
        }
        String[] redirect_uris = request.getParameterValues("redirect_uri");
        if(redirect_uris == null || redirect_uris.length != 1){
          redirectToError(response, "Required param 'redirect_uri' missing",redirectURI);
          return;
        }
        OauthClient client;
        try {
          client = oauthClientService.findByClientId(client_ids[0]);
          if(client.getRedirectURIList().contains(redirect_uris[0])){
            redirectURI = redirect_uris[0];
          }else{
            redirectToError(response, "Invalid Client",redirectURI);
            return;
          }
        } catch (OauthClientNotFoundException e) {
          redirectToError(response, "OAuth client not found",redirectURI);
          return;
        }
        String[] scope = request.getParameterValues("scope");
        if(scope == null || scope.length != 1){
          redirectToError(response, "Required param 'scope' missing",redirectURI);
          return;
        }
        String[] response_type = request.getParameterValues("response_type");
        if(response_type == null || response_type.length != 1){
          redirectToError(response, "Required param 'response_type' missing",redirectURI);
          return;
        }
        String[] state = request.getParameterValues("state");
        String[] nonce = request.getParameterValues("nonce");
        String[] access_type = request.getParameterValues("access_type");

        AuthorizeRequestDTO oauthAuthorizeRequestDTO = AuthorizeRequestDTO.builder()
            .client_id(client_ids[0])
            .redirect_uri(redirect_uris[0])
            .scope(new HashSet<>(Arrays.asList(scope[0].split(SCOPE_TOKEN))))
            .response_type(response_type[0])
            .state(state != null ? state[0] : null)
            .nonce(nonce != null ? nonce[0] : null)
            .access_type(access_type != null ? access_type[0] : null)
            .build();
        request.getSession().setAttribute(AUTH_REQ_ATTRIBUTE_REQ_PARAMS, oauthAuthorizeRequestDTO);
        request.getSession().setAttribute(AUTH_REQ_ATTRIBUTE_CLIENT, client);
      }
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected String getAlreadyFilteredAttributeName() {
    return "ONCE";
  }

  private void redirectToError(HttpServletResponse response, String errorDescription, String redirectURI) throws IOException {
    response.sendRedirect(redirectURI+"?error=invalid_request&error_description="+errorDescription+"");
  }
}
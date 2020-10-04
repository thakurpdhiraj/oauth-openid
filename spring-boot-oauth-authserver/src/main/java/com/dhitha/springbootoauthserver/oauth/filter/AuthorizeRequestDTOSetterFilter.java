package com.dhitha.springbootoauthserver.oauth.filter;

import static com.dhitha.springbootoauthserver.oauth.constant.Constants.AUTH_REQ_ATTRIBUTE_REQ_PARAMS;
import static com.dhitha.springbootoauthserver.oauth.constant.Constants.SCOPE_TOKEN;

import com.dhitha.springbootoauthserver.oauth.constant.Endpoints;
import com.dhitha.springbootoauthserver.oauth.dto.AuthorizeRequestDTO;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** @author Dhiraj */
@Component
public class AuthorizeRequestDTOSetterFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException{
    String AUTHORIZATION_V1_CONTEXT_PATH = Endpoints.AUTHORIZATION_ENDPOINT;
    if (AUTHORIZATION_V1_CONTEXT_PATH.equals(request.getServletPath())) {
      if (request.getSession().getAttribute(AUTH_REQ_ATTRIBUTE_REQ_PARAMS) == null) {

        AuthorizeRequestDTO oauthAuthorizeRequestDTO = new AuthorizeRequestDTO();
        Optional.ofNullable(request.getParameterValues("client_id"))
            .ifPresent(strings -> oauthAuthorizeRequestDTO.setClient_id(strings[0]));
        Optional.ofNullable(request.getParameterValues("redirect_uri"))
            .ifPresent(strings -> oauthAuthorizeRequestDTO.setRedirect_uri(strings[0]));
        Optional.ofNullable(request.getParameterValues("scope"))
            .ifPresent(
                strings ->
                    oauthAuthorizeRequestDTO.setScope(
                        new HashSet<>(Arrays.asList(strings[0].split(SCOPE_TOKEN)))));
        Optional.ofNullable(request.getParameterValues("response_type"))
            .ifPresent(strings -> oauthAuthorizeRequestDTO.setResponse_type(strings[0]));
        Optional.ofNullable(request.getParameterValues("state"))
            .ifPresent(strings -> oauthAuthorizeRequestDTO.setState(strings[0]));
        Optional.ofNullable(request.getParameterValues("nonce"))
            .ifPresent(strings -> oauthAuthorizeRequestDTO.setNonce(strings[0]));
        System.out.println(oauthAuthorizeRequestDTO);
        Optional.ofNullable(request.getParameterValues("access_type"))
            .ifPresent(strings -> oauthAuthorizeRequestDTO.setAccess_type(strings[0]));
        System.out.println("Filter request: "+oauthAuthorizeRequestDTO);
        request.getSession().setAttribute(AUTH_REQ_ATTRIBUTE_REQ_PARAMS, oauthAuthorizeRequestDTO);
      }
    }
    filterChain.doFilter(request, response);

  }

  @Override
  protected String getAlreadyFilteredAttributeName() {
    return "ONCE";
  }
}

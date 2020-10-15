package com.dhitha.springbootoauthserver.resource.filter;

import com.dhitha.springbootoauthserver.oauth.entity.AccessToken;
import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.error.notfound.AccessTokenNotFoundException;
import com.dhitha.springbootoauthserver.oauth.service.AccessTokenService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/** @author Dhiraj */
@Component
public class ResourceAuthenticationFilter extends OncePerRequestFilter {

  @Autowired private AccessTokenService accessTokenService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
      System.out.println("resource authentication required ");
      String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
      if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
        sendError(response);
        return;
      }
      String accessToken = authHeader.substring(7);
      try {
        AccessToken token = accessTokenService.getToken(accessToken);
        OauthClient client = token.getClient();
        List<SimpleGrantedAuthority> authorities =
            token.getApprovedScopes().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(client, null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      } catch (AccessTokenNotFoundException e) {
        sendError(response);
        return;
      }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return !request.getServletPath().startsWith("/api/resources");
  }

  private void sendError(HttpServletResponse response) throws IOException {
    JSONObject error =
        new JSONObject()
            .appendField("error", "invalid_request")
            .appendField("error_description", "Authorization Header Missing");
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(error.toJSONString());
  }
}

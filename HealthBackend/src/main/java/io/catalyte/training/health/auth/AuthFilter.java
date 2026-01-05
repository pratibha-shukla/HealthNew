package io.catalyte.training.health.auth;

import static io.catalyte.training.health.constants.StringConstants.APPLICATION_JSON;
import static io.catalyte.training.health.constants.StringConstants.AUTHORIZATION_HEADER_NAME;
import static io.catalyte.training.health.constants.StringConstants.CLAIMS_ATTRIBUTE;
import static io.catalyte.training.health.constants.StringConstants.EMAIL_ATTRIBUTE;
import static io.catalyte.training.health.constants.StringConstants.ISSUER;
import static io.catalyte.training.health.constants.StringConstants.MISSING_INVALID_ERROR;
import static io.catalyte.training.health.constants.StringConstants.ROLES_ATTRIBUTE;
import static io.catalyte.training.health.constants.StringConstants.SECRET_KEY;
import static io.catalyte.training.health.constants.StringConstants.TOKEN_PREFIX;
import static io.catalyte.training.health.constants.StringConstants.UNAUTHORIZED;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.catalyte.training.health.exceptions.ExceptionResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthFilter extends UsernamePasswordAuthenticationFilter {

  private boolean isExcludedURI(String uri) {
    return uri.equals("/login") || uri.equals("/error") || uri.equals("/swagger-ui.html");
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain)
      throws IOException, ServletException {
    final HttpServletRequest request = (HttpServletRequest) servletRequest;
    final HttpServletResponse response = (HttpServletResponse) servletResponse;
    final String authHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);

    if (!isExcludedURI(request.getRequestURI()) && authHeader == null || authHeader == null
        || !authHeader
        .startsWith(TOKEN_PREFIX)) {
      handleError(response);
      return;
    }

    final String token = authHeader.substring(7);

    try {
      Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
      JWTVerifier verifier = JWT.require(algorithm)
          .withIssuer(ISSUER)
          .build();
      DecodedJWT jwt = verifier.verify(token);
      Claim claims = jwt.getClaim(ROLES_ATTRIBUTE);
      String email = jwt.getSubject();
      request.setAttribute(CLAIMS_ATTRIBUTE, claims);
      request.setAttribute(EMAIL_ATTRIBUTE, email);
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
          email, null, Collections.singletonList(new SimpleGrantedAuthority(claims.asString())));
      SecurityContextHolder.getContext().setAuthentication(auth);
    } catch (Exception e) {
      handleError(response);
      return;
    }

    filterChain.doFilter(request, response);
  }

  private String convertObjectToJson(Object object) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(object);
  }

  private void handleError(HttpServletResponse response) throws IOException {
    ExceptionResponse exceptionResponse = new ExceptionResponse(UNAUTHORIZED, new Date(),
        MISSING_INVALID_ERROR);
    response.setContentType(APPLICATION_JSON);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.sendError(HttpServletResponse.SC_FORBIDDEN);

  }

}

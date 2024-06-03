package com.sinnts.grading.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinnts.grading.exceptions.InvalidResourceException;
import com.sinnts.grading.exceptions.ResourceNotFoundException;
import com.sinnts.grading.jwt.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/** TODO: validate via the URI which repository to delete from */
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  private final TokenRepository appUserTokenRepository;

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) {
    final String authHeader = request.getHeader(AUTHORIZATION);
    final String jwt;

    if (authHeader == null || !authHeader.startsWith("Bearer ")){
      throw new InvalidResourceException("Access Token Invalid");
    }

    jwt = authHeader.substring(7);

    var storedToken = appUserTokenRepository.findByToken(jwt).orElse(null);

    if (storedToken != null) {
      storedToken.setExpired(true);
      storedToken.setRevoked(true);
      appUserTokenRepository.save(storedToken);
      response.setStatus(OK.value());
      response.setContentType(APPLICATION_JSON_VALUE);
      try {
        new ObjectMapper().writeValue(response.getOutputStream(), Map.of("message", "Logged out Successfully"));
      } catch (IOException e) {
//        throw new (e.getMessage());
      }
    }else {
      throw new ResourceNotFoundException("Invalid Access Token");
    }
  }
}

package com.sinnts.grading.user;


import com.sinnts.grading.exceptions.InvalidResourceException;
import com.sinnts.grading.exceptions.ResourceNotFoundException;
import com.sinnts.grading.config.security.JwtService;
import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.user.dto.request.LoginRequest;
import com.sinnts.grading.user.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
  private final UserRepository userRepository;

  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public User getUserByUsername(String username) {
    return userRepository.findByUsernameIgnoreCase(username)
        .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
  }


  public ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.username(),
            request.password()
        )
    );

    var user = getUserByUsername(request.username());

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);


    var payload = new LoginResponse(
        jwtToken,
        jwtService.getJwtExpirationInMinutes(),
        refreshToken,
        jwtService.getRefreshExpirationInMinuted()
    );

    return ResponseEntity.ok(
        ApiResponse.<LoginResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Login Successfully")
            .data(
                payload
            )
            .build()
    );
  }

  public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
      HttpServletRequest request
  ) {
    final String authHeader = request.getHeader(AUTHORIZATION);
    final String refreshToken;
    final String userEmail;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new InvalidResourceException("No or Invalid refresh token");
    }

    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken); // todo: extract user email from refresh token

    if (userEmail != null) {

      var user = getUserByUsername(userEmail);

      if (jwtService.isTokenValid(refreshToken, user)) {
        var newAccessToken = jwtService.generateToken(user);

        var payload = new LoginResponse(
            newAccessToken,
            jwtService.getJwtExpirationInMinutes(),
            refreshToken,
            jwtService.getRefreshExpirationInMinuted()
        );

        return ResponseEntity.ok(
            ApiResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .status("Success")
                .message("Token Refreshed Successfully")
                .data(
                    payload
                )
                .build()
        );
      }
    }
    throw new IllegalStateException("Invalid refresh token");
  }
}

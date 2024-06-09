package com.sinnts.grading.auth;

import com.sinnts.grading.config.security.JwtService;
import com.sinnts.grading.exceptions.InvalidResourceException;
import com.sinnts.grading.jwt.Token;
import com.sinnts.grading.jwt.TokenService;
import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.user.User;
import com.sinnts.grading.user.UserService;
import com.sinnts.grading.user.dto.request.LoginRequest;
import com.sinnts.grading.user.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import static com.sinnts.grading.jwt.TokenType.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Service
public class UserAuthService {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtService jwtService;
  private final TokenService tokenService;

  public ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.username(),
            request.password()
        )
    );

    var user = userService.getUserByUsername(request.username());

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    var payload = new LoginResponse(
        jwtToken,
        jwtService.getJwtExpirationInMinutes(),
        refreshToken,
        jwtService.getRefreshExpirationInMinuted()
    );

    revokeAllUserTokens(user);

    saveUserToken(user, jwtToken);

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

  /**
   * Request for new access JWT Token using the provided Refresh Token issued at last Login
   */
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
    userEmail = jwtService.extractUsername(refreshToken);

    if (userEmail != null) {

      var user = userService.getUserByUsername(userEmail);

      if (jwtService.isTokenValid(refreshToken, user)) {
        var newAccessToken = jwtService.generateToken(user);

        revokeAllUserTokens(user);

        saveUserToken(user, newAccessToken);

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

  /**
   * TODO: <br>
   * Confirm if the JWT Token are needed to be kept or Deleted when it is not longer valid
   **/
  private void revokeAllUserTokens(User user) {
    var validTokens = tokenService.findAllValidTokenByUser(user);

    if (validTokens.isEmpty()) {
      return;
    }

    validTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });

    tokenService.saveAllTokens(validTokens);
  }

  /** Saving the generated JWT Token when a user logged in
   **/
  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(BEARER)
        .expired(false)
        .revoked(false)
        .build();

    tokenService.saveToken(token);
  }
}

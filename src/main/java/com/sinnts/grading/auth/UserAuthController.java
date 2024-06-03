package com.sinnts.grading.auth;

import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.user.UserService;
import com.sinnts.grading.user.dto.request.LoginRequest;
import com.sinnts.grading.user.dto.request.UserRegistrationRequest;
import com.sinnts.grading.user.dto.response.LoginResponse;
import com.sinnts.grading.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
@Tag(name = "System Users Authentication Controller")
public class UserAuthController {

  private final UserAuthService userAuthService;
  private final UserService userService;

  @PostMapping
  public ResponseEntity<ApiResponse<UserResponse>> createUser(
      @Valid @RequestBody UserRegistrationRequest request
  ) {
    return userAuthService.crateUser(request);
  }

  @PostMapping("login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(
      @Valid @RequestBody LoginRequest request
  ) {
    return userService.login(request);
  }

  @PostMapping("refresh-token")
  public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(HttpServletRequest request) {
    return userService.refreshToken(request);
  }

}

package com.sinnts.grading.user;

import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.universal.PagedApiResponse;
import com.sinnts.grading.user.dto.request.UpdateUserRequest;
import com.sinnts.grading.user.dto.request.UserRegistrationRequest;
import com.sinnts.grading.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller")
@SecurityRequirement(name = "BearerAuth")
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<ApiResponse<UserResponse>> createUser(
      @Valid @RequestBody UserRegistrationRequest request
  ) {
    return userService.crateUser(request);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<UserResponse>> getUserByID(
      @RequestParam UUID userId
  ) {
    return userService.getUserById(userId);
  }

  @GetMapping("/all")
  public ResponseEntity<PagedApiResponse<UserResponse>> getAllUsers(
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return userService.getAllUsers(page, size);
  }

  @PutMapping("/{userId}")
  public ResponseEntity<ApiResponse<UserResponse>> updateUser(
      @PathVariable UUID userId,
      @Valid @RequestBody UpdateUserRequest request
  ) {
    return userService.updateUser(userId, request);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<ApiResponse<UserResponse>> deleteUser(
      @PathVariable UUID userId
  ) {
    return userService.deleteUser(userId);
  }

  @GetMapping("/profile")
  public ResponseEntity<ApiResponse<UserResponse>> getUserProfile() {
   String username = SecurityContextHolder.getContext().getAuthentication().getName();
   return userService.getUserProfile(username);
  }
}

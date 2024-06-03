package com.sinnts.grading.auth;

import com.sinnts.grading.exceptions.UserExistsByUsername;
import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.user.User;
import com.sinnts.grading.user.UserRepository;
import com.sinnts.grading.user.dto.request.UserRegistrationRequest;
import com.sinnts.grading.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.sinnts.grading.mpastruct.MapstructMapper.INSTANCE;

@RequiredArgsConstructor
@Service
public class UserAuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public ResponseEntity<ApiResponse<UserResponse>> crateUser(UserRegistrationRequest request) {
    if (
        userRepository.existsByUsernameIgnoreCase(request.username())
    ) throw new UserExistsByUsername("User With the Username [ %s ] Already Exists"
        .formatted(request.username()));

    User user = User.builder()
        .username(request.username())
        .password(passwordEncoder.encode(request.password()))
        .fullName(request.fullName())
        .role(request.role())
        .build();

    userRepository.save(user);

    UserResponse userResponse = INSTANCE.userToUserResponse(user);

    return ResponseEntity.ok(
        ApiResponse.<UserResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("User With Role [ %s ] Created Successfully".formatted(request.role()))
            .data(
                userResponse
            )
            .build()
    );
  }
}

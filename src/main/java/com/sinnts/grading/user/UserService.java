package com.sinnts.grading.user;


import com.sinnts.grading.exceptions.ResourceNotFoundException;
import com.sinnts.grading.exceptions.UserExistsByUsername;
import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.universal.PagedApiResponse;
import com.sinnts.grading.user.dto.request.UpdateUserRequest;
import com.sinnts.grading.user.dto.request.UserRegistrationRequest;
import com.sinnts.grading.user.dto.response.UserResponse;
import com.sinnts.grading.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sinnts.grading.mpastruct.MapstructMapper.INSTANCE;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
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

  public User getUserByUsername(String username) {
    return userRepository.findByUsernameIgnoreCase(username)
        .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
  }

  public ResponseEntity<PagedApiResponse<UserResponse>> getAllUsers(int page, int size) {

    Pageable pageable = PaginationUtils.getPageable(page, size);
    Page<User> usersPage = userRepository.findAll(pageable);

    if (usersPage.getContent().isEmpty())
      throw new ResourceNotFoundException("No Users Records Found");

    List<UserResponse> userResponses = usersPage.getContent().stream().map(INSTANCE::userToUserResponse).toList();

    return ResponseEntity.ok(
        PagedApiResponse.<UserResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Performances Retrieved successfully")
            .data(
                userResponses
            )
            .pageNumber(page == 0 ? 1 : page)
            .totalPages(usersPage.getTotalPages())
            .isLastPage(usersPage.isLast())
            .build()
    );
  }

  public ResponseEntity<ApiResponse<UserResponse>> getUserById(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User with the ID [ %s ] not found".formatted(userId)));

    UserResponse userResponse = INSTANCE.userToUserResponse(user);
    return ResponseEntity.ok(
        ApiResponse.<UserResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("User With ID [ %s ] Fetched Successfully".formatted(userId))
            .data(
                userResponse
            )
            .build()
    );
  }

  public ResponseEntity<ApiResponse<UserResponse>> updateUser(UUID userId, UpdateUserRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User with ID [ %s ] not found".formatted(userId)));

    user.setFullName(request.newFullName());
    userRepository.save(user);

    UserResponse userResponse = INSTANCE.userToUserResponse(user);
    return ResponseEntity.ok(
        ApiResponse.<UserResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("User With ID [ %s ] Updated Successfully".formatted(userId))
            .data(
                userResponse
            )
            .build()
    );
  }

  public ResponseEntity<ApiResponse<UserResponse>> deleteUser(UUID userId) {
    User user = userRepository
        .findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with ID [ %s ] not found".formatted(userId)));

    userRepository.delete(user);

    UserResponse userResponse = INSTANCE.userToUserResponse(user);
    return ResponseEntity.ok(
        ApiResponse.<UserResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("User With ID [ %s ] Deleted Successfully".formatted(userId))
            .data(
                userResponse
            )
            .build()
    );
  }

  public ResponseEntity<ApiResponse<UserResponse>> getUserProfile(String username) {
    User user = userRepository
        .findByUsernameIgnoreCase(username)
        .orElseThrow(() -> new ResourceNotFoundException("User with Username [ %s ] not found".formatted(username)));

    UserResponse userResponse = INSTANCE.userToUserResponse(user);
    return ResponseEntity.ok(
        ApiResponse.<UserResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("User Profile Updated Successfully")
            .data(
                userResponse
            )
            .build()
    );
  }
}

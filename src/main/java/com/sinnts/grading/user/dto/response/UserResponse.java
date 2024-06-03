package com.sinnts.grading.user.dto.response;

import com.sinnts.grading.user.enums.Role;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.sinnts.grading.user.User}
 */
public record UserResponse(
    UUID id,
    String fullName,
    String username,
    Role role,
    boolean enabled,
    boolean locked,
    LocalDateTime createdDate,
    LocalDateTime lastModifiedDate
) implements Serializable {
}
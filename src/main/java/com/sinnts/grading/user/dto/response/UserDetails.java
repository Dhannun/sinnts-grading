package com.sinnts.grading.user.dto.response;

import com.sinnts.grading.user.enums.Role;

import java.io.Serializable;

/**
 * DTO for {@link com.sinnts.grading.user.User}
 */
public record UserDetails(
    String fullName,
    String username,
    Role role
) implements Serializable {
}
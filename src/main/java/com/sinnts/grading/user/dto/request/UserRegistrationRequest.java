package com.sinnts.grading.user.dto.request;

import com.sinnts.grading.user.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.sinnts.grading.user.User}
 */
public record UserRegistrationRequest(
    @NotEmpty(message = "Full name is Required")
    @NotBlank(message = "Full name is Required")
    String fullName,
    @NotEmpty(message = "Username is Required")
    @NotBlank(message = "Username is Required")
    String username,
    @NotEmpty(message = "Password is Required")
    @NotBlank(message = "Password is Required")
    String password,
    @NotNull(message = "Role is Required")
    Role role
) implements Serializable {
}
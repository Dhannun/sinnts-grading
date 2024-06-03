package com.sinnts.grading.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
    @NotEmpty(message = "Username is Required")
    @NotBlank(message = "Username is Required")
    String username,
    @NotEmpty(message = "Password is Required")
    @NotBlank(message = "Password is Required")
    String password
) {
}

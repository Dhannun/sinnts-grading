package com.sinnts.grading.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public record UpdateUserRequest(
    @NotEmpty(message = "New full name is Required")
    @NotBlank(message = "New full name is Required")
    String newFullName
) implements Serializable {
}

package com.sinnts.grading.department.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public record AddDepartmentRequest(
    @NotEmpty(message = "Department name is Required")
    @NotBlank(message = "Department name is Required")
    String name
) implements Serializable {
}
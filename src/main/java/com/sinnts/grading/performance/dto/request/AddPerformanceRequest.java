package com.sinnts.grading.performance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record AddPerformanceRequest(
    @NotEmpty(message = "Performance name is Required")
    @NotBlank(message = "Performance name is Required")
    String name,
    List<UUID> departmentIds
) implements Serializable {
}
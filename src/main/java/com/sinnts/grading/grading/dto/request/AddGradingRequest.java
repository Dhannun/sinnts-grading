package com.sinnts.grading.grading.dto.request;

import com.sinnts.grading.grading.enums.Grade;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddGradingRequest(
    @NotNull(message = "Staff Id is Required")
    UUID staffId,
    @NotNull(message = "Performance Id is Required")
    UUID performanceId,
    @NotNull(message = "Grade is Required")
    Grade grade
) {
}

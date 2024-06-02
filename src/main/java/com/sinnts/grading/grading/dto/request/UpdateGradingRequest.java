package com.sinnts.grading.grading.dto.request;

import com.sinnts.grading.grading.enums.Grade;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateGradingRequest(
    @NotNull(message = "New Grade is Required")
    Grade newGrade
) {
}

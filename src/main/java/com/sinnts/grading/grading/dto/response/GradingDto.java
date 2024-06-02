package com.sinnts.grading.grading.dto.response;

import com.sinnts.grading.grading.enums.Grade;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.sinnts.grading.grading.Grading}
 */
public record GradingDto(
    UUID id,
    Grade grade,
    LocalDateTime createdDate,
    LocalDateTime lastModifiedDate
) implements Serializable {
}
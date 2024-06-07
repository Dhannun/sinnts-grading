package com.sinnts.grading.performance.dto.response;

import com.sinnts.grading.user.dto.response.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.sinnts.grading.performance.Performance}
 */
public record PerformanceResponse(
    UUID id,
    String name,
    LocalDateTime createdDate,
    LocalDateTime lastModifiedDate,
    UserDetails createdBy,
    UserDetails lastModifiedBy
) implements Serializable {
}
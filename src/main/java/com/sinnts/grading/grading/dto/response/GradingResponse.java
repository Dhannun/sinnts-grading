package com.sinnts.grading.grading.dto.response;

import com.sinnts.grading.performance.dto.response.PerformanceResponse;
import com.sinnts.grading.staff.dto.response.StaffResponse;
import com.sinnts.grading.user.dto.response.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.sinnts.grading.grading.Grading}
 */
public record GradingResponse(
    UUID id,
    PerformanceResponse performance,
    GradeDetails grade,
    LocalDateTime createdDate,
    LocalDateTime lastModifiedDate,
    StaffResponse staff,
    UserDetails createdBy,
    UserDetails lastModifiedBy
) implements Serializable {
}
package com.sinnts.grading.department.dto.response;

import com.sinnts.grading.user.dto.response.UserDetails;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.sinnts.grading.department.Department}
 */

public record DepartmentResponse(
    UUID id,
    String name,
    LocalDateTime createdDate,
    LocalDateTime lastModifiedDate,
    UserDetails createdBy,
    UserDetails lastModifiedBy
) {
}
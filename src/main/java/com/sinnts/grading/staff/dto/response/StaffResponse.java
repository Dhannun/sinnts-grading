package com.sinnts.grading.staff.dto.response;

import com.sinnts.grading.department.dto.response.DepartmentResponse;
import com.sinnts.grading.staff.Staff;
import com.sinnts.grading.user.dto.response.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link Staff}
 */
public record StaffResponse(
    UUID id,
    String fullName,
    String title,
    String identity,
    LocalDateTime createdDate,
    LocalDateTime lastModifiedDate,
    DepartmentResponse department,
    UserDetails createdBy,
    UserDetails lastModifiedBy
) implements Serializable {
}
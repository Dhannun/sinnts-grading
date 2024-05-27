package com.sinnts.grading.department.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.sinnts.grading.department.Department}
 */
@Getter
@Setter
@Builder
public class DepartmentResponse{
    private UUID id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
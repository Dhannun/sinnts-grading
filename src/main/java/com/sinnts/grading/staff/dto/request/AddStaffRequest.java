package com.sinnts.grading.staff.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AddStaffRequest {
  @NotEmpty(message = "Full name is Required")
  @NotBlank(message = "Full name is Required")
  private String fullName;
  @NotEmpty(message = "Title is Required")
  @NotBlank(message = "Title is Required")
  private String title;
  @NotEmpty(message = "Staff ID is Required")
  @NotBlank(message = "Staff ID is Required")
  private String identity;
  @NotEmpty(message = "Department Id is Required")
  @NotBlank(message = "Department Id is Required")
  private UUID departmentId;
}

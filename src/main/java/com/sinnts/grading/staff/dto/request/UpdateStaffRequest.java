package com.sinnts.grading.staff.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateStaffRequest {
  private String newFullName;
  private String newTitle;
}

package com.sinnts.grading.mpastruct;

import com.sinnts.grading.department.Department;
import com.sinnts.grading.department.dto.response.DepartmentResponse;
import com.sinnts.grading.grading.Grading;
import com.sinnts.grading.grading.dto.response.GradingResponse;
import com.sinnts.grading.performance.Performance;
import com.sinnts.grading.performance.dto.response.PerformanceResponse;
import com.sinnts.grading.staff.Staff;
import com.sinnts.grading.staff.dto.response.StaffResponse;
import com.sinnts.grading.user.User;
import com.sinnts.grading.user.UserService;
import com.sinnts.grading.user.dto.response.UserResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface MapstructMapper {
  MapstructMapper INSTANCE = Mappers.getMapper(MapstructMapper.class);

  DepartmentResponse departmentTODepartmentResponse(Department department);

  PerformanceResponse performanceToPerformanceResponse(Performance performance);

  StaffResponse staffToStaffDto(Staff staff);

  GradingResponse gradingToGradingResponse(Grading grading);

  UserResponse userToUserResponse(User user);

}

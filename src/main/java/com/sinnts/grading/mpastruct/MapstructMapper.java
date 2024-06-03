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
import com.sinnts.grading.user.dto.response.UserResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapstructMapper {
  MapstructMapper INSTANCE = Mappers.getMapper(MapstructMapper.class);


  DepartmentResponse departmentTODepartmentResponse(Department department);

  PerformanceResponse performanceToPerformanceResponse(Performance performance);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Performance partialUpdate(PerformanceResponse performanceResponse, @MappingTarget Performance performance);

  StaffResponse staffToStaffDto(Staff staff);

  GradingResponse gradingToGradingResponse(Grading grading);

  UserResponse userToUserResponse(User user);
}

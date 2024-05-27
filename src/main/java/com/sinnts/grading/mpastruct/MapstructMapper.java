package com.sinnts.grading.mpastruct;

import com.sinnts.grading.department.Department;
import com.sinnts.grading.department.dto.response.DepartmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapstructMapper {
  MapstructMapper INSTANCE = Mappers.getMapper(MapstructMapper.class);


  DepartmentResponse departmentTODepartmentResponse(Department department);
}

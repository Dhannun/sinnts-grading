package com.sinnts.grading.mpastruct;

import com.sinnts.grading.department.Department;
import com.sinnts.grading.department.dto.response.DepartmentResponse;
import com.sinnts.grading.performance.Performance;
import com.sinnts.grading.performance.dto.response.PerformanceResponse;
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
}

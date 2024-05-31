package com.sinnts.grading.performance;

import com.sinnts.grading.department.Department;
import com.sinnts.grading.department.DepartmentRepository;
import com.sinnts.grading.exceptions.InvalidResourceException;
import com.sinnts.grading.exceptions.ResourceNotFoundException;
import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.universal.PagedApiResponse;
import com.sinnts.grading.performance.dto.request.AddPerformanceRequest;
import com.sinnts.grading.performance.dto.request.UpdatePerformanceRequest;
import com.sinnts.grading.performance.dto.response.PerformanceResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.sinnts.grading.mpastruct.MapstructMapper.INSTANCE;

@Service
@RequiredArgsConstructor
public class PerformanceService {

  private final PerformanceRepository performanceRepository;
  private final DepartmentRepository departmentRepository;

  public Performance getPerformanceById(UUID performanceId) {
    return performanceRepository.findById(performanceId)
        .orElseThrow(() -> new ResourceNotFoundException("Performance with ID [ %S ] not found".formatted(performanceId)));
  }

  @Transactional
  public ResponseEntity<ApiResponse<PerformanceResponse>> createPerformance(AddPerformanceRequest request) {
    Set<Department> departments = new HashSet<>();

    // Check if department ID(s) is provided or not
    if (!request.departmentIds().isEmpty()) {
      for (UUID departmentId : request.departmentIds()) {
        Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Department with ID [ %s ] not found".formatted(departmentId)));
        departments.add(department);
      }
    }

    Performance performance = Performance.builder()
        .name(request.name())
        .departments(departments)
        .build();
    performanceRepository.save(performance);

    for (Department department : departments)
      department.getPerformances().add(performance);

    PerformanceResponse performanceResponse = INSTANCE.performanceToPerformanceResponse(performance);

    return ResponseEntity.ok(
        ApiResponse.<PerformanceResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Performance Created Successfully ")
            .data(
                performanceResponse
            )
            .build()
    );
  }

  public ResponseEntity<PagedApiResponse<PerformanceResponse>> getAllPerformance(int page, int size) {
    page = page == 0 ? 0 : page - 1;
    Pageable pageable = PageRequest.of(page, size);
    Page<Performance> performancePages = performanceRepository.findAllOrderByCreatedDateDesc(pageable);

    if (performancePages.getContent().isEmpty()) throw new ResourceNotFoundException("No Department Records Found");

    return getPagedPerformanceResponse(page, performancePages);
  }

  public ResponseEntity<ApiResponse<PerformanceResponse>> getPerformance(UUID performanceId) {
    Performance performance = getPerformanceById(performanceId);
    PerformanceResponse performanceResponse = INSTANCE.performanceToPerformanceResponse(performance);

    return ResponseEntity.ok(
        ApiResponse.<PerformanceResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Performance Retrieved successfully")
            .data(
                performanceResponse
            )
            .build()
    );
  }

  @Transactional
  public ResponseEntity<ApiResponse<PerformanceResponse>> updatePerformance(UUID performanceId, UpdatePerformanceRequest request) {
    Performance performance = getPerformanceById(performanceId);
    performance.setName(request.newName());
    performanceRepository.save(performance);
    PerformanceResponse performanceResponse = INSTANCE.performanceToPerformanceResponse(performance);
    return ResponseEntity.ok(
        ApiResponse.<PerformanceResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Performance Retrieved successfully")
            .data(
                performanceResponse
            )
            .build()
    );
  }

  public ResponseEntity<ApiResponse<PerformanceResponse>> deletePerformance(UUID performanceId) {
    Performance performance = getPerformanceById(performanceId);
    performanceRepository.delete(performance);
    PerformanceResponse performanceResponse = INSTANCE.performanceToPerformanceResponse(performance);
    return ResponseEntity.ok(
        ApiResponse.<PerformanceResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Performance Deleted successfully")
            .data(
                performanceResponse
            )
            .build()
    );
  }

  @Transactional
  public ResponseEntity<ApiResponse<PerformanceResponse>> addDepartment(UUID performanceId, UUID departmentId) {
    Performance performance = getPerformanceById(performanceId);
    Department department = departmentRepository.findById(departmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Department with ID [ %s ] not found".formatted(departmentId)));

    Set<Department> departments = performance.getDepartments();

    if (departments.contains(department))
      throw new InvalidResourceException("Performance [ %s ] already exists in department [ %s ]"
          .formatted(performance.getName(), department.getName()));

    departments.add(department);
    performance.setDepartments(
        departments
    );

    departmentRepository.save(department);

    Set<Performance> performances = department.getPerformances();
    performances.add(performance);
    department.setPerformances(
        performances
    );

    performanceRepository.save(performance);

    PerformanceResponse performanceResponse = INSTANCE.performanceToPerformanceResponse(performance);
    return ResponseEntity.ok(
        ApiResponse.<PerformanceResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Department [ %s ] Added to the Performance successfully".formatted(department.getName()))
            .data(
                performanceResponse
            )
            .build()
    );
  }

  @Transactional
  public ResponseEntity<ApiResponse<PerformanceResponse>> removeDepartment(UUID performanceId, UUID departmentId) {
    Performance performance = getPerformanceById(performanceId);
    Set<Department> departments = performance.getDepartments();

    Department departmentToRemoved = departments.stream().filter(
            department -> departmentId.equals(department.getId())
        ).findAny()
        .orElseThrow(() -> new InvalidResourceException("Performance [ %s ] does not belong to Department with ID [ %s ]"
            .formatted(performance.getName(), departmentId)));

    departments.remove(departmentToRemoved);
    performance.setDepartments(departments);

    performanceRepository.save(performance);

    PerformanceResponse performanceResponse = INSTANCE.performanceToPerformanceResponse(performance);
    return ResponseEntity.ok(
        ApiResponse.<PerformanceResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Performance Removed from the Department with ID [ %s ]".formatted(departmentId))
            .data(
                performanceResponse
            )
            .build()
    );
  }

  public ResponseEntity<PagedApiResponse<PerformanceResponse>> getDepartmentPerformance(UUID departmentId, int page, int size) {
    page = page == 0 ? 0 : page - 1;
    Pageable pageable = PageRequest.of(page, size);
    Page<Performance> performancePages = performanceRepository.findByDepartments_Id(departmentId, pageable);

    if (performancePages.getContent().isEmpty())
      throw new ResourceNotFoundException("No Department Contents found");

    return getPagedPerformanceResponse(page, performancePages);
  }

  @NotNull
  private ResponseEntity<PagedApiResponse<PerformanceResponse>> getPagedPerformanceResponse(int page, Page<Performance> performancePages) {
    List<PerformanceResponse> performanceResponses = performancePages.getContent().stream().map(INSTANCE::performanceToPerformanceResponse).toList();

    return ResponseEntity.ok(
        PagedApiResponse.<PerformanceResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Performances Retrieved successfully")
            .data(
                performanceResponses
            )
            .pageNumber(page + 1)
            .totalPages(performancePages.getTotalPages())
            .isLastPage(performancePages.isLast())
            .build()
    );
  }
}

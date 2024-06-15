package com.sinnts.grading.department;

import com.sinnts.grading.department.dto.request.AddDepartmentRequest;
import com.sinnts.grading.department.dto.request.UpdateDepartmentRequest;
import com.sinnts.grading.department.dto.response.DepartmentResponse;
import com.sinnts.grading.exceptions.ResourceNotFoundException;
import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.universal.PagedApiResponse;
import com.sinnts.grading.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.sinnts.grading.mpastruct.MapstructMapper.INSTANCE;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {

  private final DepartmentRepository departmentRepository;

  public Department getDepartmentByID(UUID departmentId) {
    log.info("Getting department by ID: {}", departmentId);
    return departmentRepository.findById(departmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Department With ID [ %s ] not found".formatted(departmentId)));
  }

  public ResponseEntity<ApiResponse<DepartmentResponse>> addDepartment(AddDepartmentRequest request) {
    Department department = Department.builder()
        .name(request.name())
        .build();
    departmentRepository.save(department);
    DepartmentResponse departmentResponse = INSTANCE.departmentTODepartmentResponse(department);
    return ResponseEntity.ok(
        ApiResponse.<DepartmentResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Department Created successfully")
            .data(
                departmentResponse
            )
            .build()
    );
  }

  public ResponseEntity<PagedApiResponse<DepartmentResponse>> getAllDepartments(int page, int size) {

    Pageable pageable = PaginationUtils.getPageable(page, size);
    Page<Department> departments = departmentRepository.findAllOrderByCreatedDateDesc(pageable);

    if (departments.getContent().isEmpty()) throw new ResourceNotFoundException("No Department Records Found");

    List<DepartmentResponse> departmentResponseList = departments.getContent().stream().map(INSTANCE::departmentTODepartmentResponse).toList();

    return ResponseEntity.ok(
        PagedApiResponse.<DepartmentResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("All Departments Fetched successfully")
            .data(
                departmentResponseList
            )
            .pageNumber(page + 1)
            .totalPages(departments.getTotalPages())
            .isLastPage(departments.isLast())
            .build()
    );
  }

  public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartmentName(UUID departmentId, UpdateDepartmentRequest request) {
    Department department = getDepartmentByID(departmentId);
    department.setName(request.newName());
    departmentRepository.save(department);
    DepartmentResponse departmentResponse = INSTANCE.departmentTODepartmentResponse(department);
    return ResponseEntity.ok(
        ApiResponse.<DepartmentResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Department Retrieved successfully")
            .data(
                departmentResponse
            )
            .build()
    );
  }

  public ResponseEntity<ApiResponse<DepartmentResponse>> deleteDepartment(UUID departmentId) {
    Department department = getDepartmentByID(departmentId);
    departmentRepository.delete(department);
    DepartmentResponse departmentResponse = INSTANCE.departmentTODepartmentResponse(department);
    return ResponseEntity.ok(
        ApiResponse.<DepartmentResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Department Deleted successfully")
            .data(
                departmentResponse
            )
            .build()
    );
  }

  public ResponseEntity<ApiResponse<DepartmentResponse>> getOneDepartmentById(UUID departmentId) {
    Department department = getDepartmentByID(departmentId);
    DepartmentResponse departmentResponse = INSTANCE.departmentTODepartmentResponse(department);
    return ResponseEntity.ok(
        ApiResponse.<DepartmentResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Department Retrieved successfully")
            .data(
                departmentResponse
            )
            .build()
    );
  }
}

package com.sinnts.grading.department;

import com.sinnts.grading.department.dto.request.AddDepartmentRequest;
import com.sinnts.grading.department.dto.response.DepartmentResponse;
import com.sinnts.grading.exceptions.ResourceNotFoundException;
import com.sinnts.grading.iniversal.ApiResponse;
import com.sinnts.grading.iniversal.PagedApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.sinnts.grading.mpastruct.MapstructMapper.INSTANCE;

@Service
@RequiredArgsConstructor
public class DepartmentService {

  private final DepartmentRepository departmentRepository;

  public Department getDepartmentByID(UUID departmentId) {
    return departmentRepository.findById(departmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Department With ID [ %s ] not found".formatted(departmentId)));
  }

  public ResponseEntity<ApiResponse<DepartmentResponse>> addCreateDepartment(AddDepartmentRequest request) {
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
    page = page == 0 ? 0 : page - 1;
    Pageable pageable = PageRequest.of(page, size);
    Page<Department> departments = departmentRepository.findAllOrderByCreatedDateDesc(pageable);

    if (departments.getContent().isEmpty()) throw new ResourceNotFoundException("No Department Records Found");

    List<DepartmentResponse> departmentResponseList = departments.getContent().stream().map(INSTANCE::departmentTODepartmentResponse).toList();

    return ResponseEntity.ok(
        PagedApiResponse.<DepartmentResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Department Created successfully")
            .data(
                departmentResponseList
            )
            .pageNumber(page + 1)
            .totalPages(departments.getTotalPages())
            .isLastPage(departments.isLast())
            .build()
    );
  }

  public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartmentName(UUID departmentId, AddDepartmentRequest request) {
    Department department = getDepartmentByID(departmentId);
    department.setName(request.name());
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
}

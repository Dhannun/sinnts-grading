package com.sinnts.grading.department;

import com.sinnts.grading.department.dto.request.AddDepartmentRequest;
import com.sinnts.grading.department.dto.response.DepartmentResponse;
import com.sinnts.grading.iniversal.ApiResponse;
import com.sinnts.grading.iniversal.PagedApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@Tag(name = "Department Controller")
public class DepartmentController {

  private final DepartmentService departmentService;

  @PostMapping
  public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
      @Valid @RequestBody AddDepartmentRequest request
  ) {
    return departmentService.addCreateDepartment(request);
  }

  @GetMapping
  public ResponseEntity<PagedApiResponse<DepartmentResponse>> getAllDepartments(
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return departmentService.getAllDepartments(page, size);
  }

  @PutMapping("/{departmentId}")
  public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartmentName(
      @PathVariable UUID departmentId,
      @RequestBody AddDepartmentRequest request
  ) {
    return departmentService.updateDepartmentName(departmentId, request);
  }

  @DeleteMapping("/{departmentId}")
  public ResponseEntity<ApiResponse<DepartmentResponse>> deleteDepartmentName(
      @PathVariable UUID departmentId
  ) {
    return departmentService.deleteDepartment(departmentId);
  }
}

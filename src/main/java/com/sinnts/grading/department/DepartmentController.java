package com.sinnts.grading.department;

import com.sinnts.grading.department.dto.request.AddDepartmentRequest;
import com.sinnts.grading.department.dto.request.UpdateDepartmentRequest;
import com.sinnts.grading.department.dto.response.DepartmentResponse;
import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.universal.PagedApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@Tag(name = "Department Controller")
@SecurityRequirement(name = "BearerAuth")
public class DepartmentController {

  private final DepartmentService departmentService;

  @PostMapping
  public ResponseEntity<ApiResponse<DepartmentResponse>> addDepartment(
      @Valid @RequestBody AddDepartmentRequest request
  ) {
    return departmentService.addDepartment(request);
  }

  @GetMapping("/all")
  public ResponseEntity<PagedApiResponse<DepartmentResponse>> getAllDepartments(
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return departmentService.getAllDepartments(page, size);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(
      @RequestParam UUID departmentId
  ) {
    return departmentService.getOneDepartmentById(departmentId);
  }

  @PutMapping("/{departmentId}")
  public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartmentName(
      @PathVariable UUID departmentId,
      @RequestBody UpdateDepartmentRequest request
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

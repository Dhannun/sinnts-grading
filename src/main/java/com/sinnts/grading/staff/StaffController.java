package com.sinnts.grading.staff;

import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.universal.PagedApiResponse;
import com.sinnts.grading.staff.dto.request.AddStaffRequest;
import com.sinnts.grading.staff.dto.request.UpdateStaffRequest;
import com.sinnts.grading.staff.dto.response.StaffResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/staffs")
@RequiredArgsConstructor
@Tag(name = "Staff Controller")
public class StaffController {

  private final StaffService staffService;

  @PostMapping
  public ResponseEntity<ApiResponse<StaffResponse>> addStaff(
      @Valid @RequestBody AddStaffRequest request
  ) {
    return staffService.addStaff(request);
  }

  @GetMapping("/all")
  public ResponseEntity<PagedApiResponse<StaffResponse>> getAllStaff(
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return staffService.getAllStaff(page, size);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<StaffResponse>> getStaffById(
      @RequestParam UUID staffId
  ) {
    return staffService.getOneStaffById(staffId);
  }

  @PutMapping("/{staffId}")
  public ResponseEntity<ApiResponse<StaffResponse>> updateStaff(
      @PathVariable UUID staffId,
      @RequestBody UpdateStaffRequest request
  ) {
    return staffService.updateStaff(staffId, request);
  }

  @DeleteMapping("/{staffId}")
  public ResponseEntity<ApiResponse<StaffResponse>> deleteStaff(
      @PathVariable UUID staffId
  ) {
    return staffService.deleteStaff(staffId);
  }

  @PutMapping("/change-department/{staffId}/{newDepartmentId}")
  public ResponseEntity<ApiResponse<StaffResponse>> changeStaffDepartment(
      @PathVariable UUID staffId,
      @PathVariable UUID newDepartmentId
  ) {
    return staffService.changeStaffDepartment(staffId, newDepartmentId);
  }


}

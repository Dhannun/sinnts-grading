package com.sinnts.grading.staff;

import com.sinnts.grading.department.Department;
import com.sinnts.grading.department.DepartmentRepository;
import com.sinnts.grading.exceptions.InvalidResourceException;
import com.sinnts.grading.exceptions.ResourceNotFoundException;
import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.universal.PagedApiResponse;
import com.sinnts.grading.staff.dto.request.AddStaffRequest;
import com.sinnts.grading.staff.dto.request.UpdateStaffRequest;
import com.sinnts.grading.staff.dto.response.StaffResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.sinnts.grading.mpastruct.MapstructMapper.INSTANCE;

@Service
@RequiredArgsConstructor
public class StaffService {
  private final DepartmentRepository departmentRepository;
  private final StaffRepository staffRepository;

  public ResponseEntity<ApiResponse<StaffResponse>> addStaff(AddStaffRequest request) {

    boolean staffExistsById = staffRepository.existsByIdentity(request.getIdentity());

    if (staffExistsById)
      throw new InvalidResourceException("Staff with ID [ %s ] Already Exists".formatted(request.getIdentity()));

    Department department = departmentRepository.findById(request.getDepartmentId())
        .orElseThrow(() -> new ResourceNotFoundException("Department with ID [ %s ] not found".formatted(request.getDepartmentId())));

    Staff staff = Staff.builder()
        .fullName(request.getFullName())
        .title(request.getTitle())
        .identity(request.getIdentity())
        .department(department)
        .build();

    staffRepository.save(staff);

    Set<Staff> staffs = department.getStaffs();
    staffs.add(staff);
    department.setStaffs(staffs);

    departmentRepository.save(department);

    StaffResponse staffResponse = INSTANCE.staffToStaffDto(staff);

    return ResponseEntity.ok(
        ApiResponse.<StaffResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Staff Created successfully")
            .data(
                staffResponse
            )
            .build()
    );
  }

  public ResponseEntity<PagedApiResponse<StaffResponse>> getAllStaff(int page, int size) {
    page = page == 0 ? 0 : page - 1;
    Pageable pageable = PageRequest.of(page, size);
    Page<Staff> staffsPage = staffRepository.findAllOrderByCreatedDateDesc(pageable);

    if (staffsPage.getContent().isEmpty()) throw new ResourceNotFoundException("No staff record found");

    List<StaffResponse> staffResponses = staffsPage.getContent().stream().map(INSTANCE::staffToStaffDto).toList();

    return ResponseEntity.ok(
        PagedApiResponse.<StaffResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("All Staffs Fetched successfully")
            .data(
                staffResponses
            )
            .pageNumber(page + 1)
            .totalPages(staffsPage.getTotalPages())
            .isLastPage(staffsPage.isLast())
            .build()
    );
  }

  public ResponseEntity<ApiResponse<StaffResponse>> getOneStaffById(UUID staffId) {
    Staff staff = staffRepository.findById(staffId)
        .orElseThrow(() -> new ResourceNotFoundException("Staff with ID [ %s ] not found".formatted(staffId)));

    StaffResponse staffResponse = INSTANCE.staffToStaffDto(staff);

    return ResponseEntity.ok(
        ApiResponse.<StaffResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Staff Fetched successfully")
            .data(
                staffResponse
            )
            .build()
    );
  }

  @Transactional
  public ResponseEntity<ApiResponse<StaffResponse>> updateStaff(UUID staffId, UpdateStaffRequest request) {
    Staff staff = staffRepository.findById(staffId)
        .orElseThrow(() -> new ResourceNotFoundException("Staff with ID [ %s ] not found".formatted(staffId)));
    if (
        !request.getNewFullName().isEmpty() &&
            !request.getNewFullName().isBlank() &&
            !request.getNewFullName().equals(staff.getFullName())
    ) staff.setFullName(request.getNewFullName());

    if (
        !request.getNewTitle().isEmpty() &&
            !request.getNewTitle().isBlank() &&
            !request.getNewTitle().equals(staff.getTitle())
    ) staff.setTitle(request.getNewTitle());

    staffRepository.save(staff);

    StaffResponse staffResponse = INSTANCE.staffToStaffDto(staff);

    return ResponseEntity.ok(
        ApiResponse.<StaffResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Staff Details Updated successfully")
            .data(
                staffResponse
            )
            .build()
    );
  }

  public ResponseEntity<ApiResponse<StaffResponse>> deleteStaff(UUID staffId) {
    Staff staff = staffRepository.findById(staffId)
        .orElseThrow(() -> new ResourceNotFoundException("Staff with ID [ %s ] not found".formatted(staffId)));

    staffRepository.delete(staff);

    StaffResponse deletedStaff = INSTANCE.staffToStaffDto(staff);

    return ResponseEntity.ok(
        ApiResponse.<StaffResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Staff and All Associated Grading Deleted successfully")
            .data(
                deletedStaff
            )
            .build()
    );
  }

  @Transactional
  public ResponseEntity<ApiResponse<StaffResponse>> changeStaffDepartment(UUID staffId, UUID newDepartmentId) {
    Staff staff = staffRepository.findById(staffId)
        .orElseThrow(() -> new ResourceNotFoundException("Staff with ID [ %s ] not found".formatted(staffId)));

    Department newDepartment = departmentRepository.findById(newDepartmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Department with ID [ %s ] not found".formatted(newDepartmentId)));

    staff.setDepartment(newDepartment);

    StaffResponse staffResponse = INSTANCE.staffToStaffDto(staff);

    return ResponseEntity.ok(
        ApiResponse.<StaffResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("%s Moved to %s Department Successfully".formatted(staff.getFullName(), newDepartment.getName()))
            .data(
                staffResponse
            )
            .build()
    );
  }
}

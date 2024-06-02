package com.sinnts.grading.grading;

import com.sinnts.grading.exceptions.ResourceNotFoundException;
import com.sinnts.grading.grading.dto.request.AddGradingRequest;
import com.sinnts.grading.grading.dto.request.UpdateGradingRequest;
import com.sinnts.grading.grading.dto.response.GradingResponse;
import com.sinnts.grading.performance.Performance;
import com.sinnts.grading.performance.PerformanceRepository;
import com.sinnts.grading.staff.Staff;
import com.sinnts.grading.staff.StaffRepository;
import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.universal.PagedApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.sinnts.grading.mpastruct.MapstructMapper.INSTANCE;

@Service
@RequiredArgsConstructor
public class GradingService {

  private final StaffRepository staffRepository;
  private final PerformanceRepository performanceRepository;
  private final GradingRepository gradingRepository;

  @Transactional
  public ResponseEntity<ApiResponse<GradingResponse>> addNewGrading(AddGradingRequest request) {
    Staff staff = staffRepository.findById(request.staffId())
        .orElseThrow(() -> new ResourceNotFoundException("Staff with ID [ %s ] not found".formatted(request.staffId())));

    Performance performance = performanceRepository.findById(request.performanceId())
        .orElseThrow(() -> new ResourceNotFoundException("Performance with ID [ %s ] not found".formatted(request.performanceId())));

    Grading grading = Grading.builder()
        .grade(request.grade())
        .performance(performance)
        .staff(staff)
        .build();

    gradingRepository.save(grading);

    staff.setGradings(staff.getGradings());
    staffRepository.save(staff);

    GradingResponse gradingResponse = INSTANCE.gradingToGradingResponse(grading);

    return ResponseEntity.ok(
        ApiResponse.<GradingResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Staff [ %s ] from [ %s ] Department Graded on [ %s ] Performance Successfully"
                .formatted(staff.getFullName(), staff.getDepartment().getName(), performance.getName()))
            .data(
                gradingResponse
            )
            .build()
    );
  }

  public ResponseEntity<PagedApiResponse<GradingResponse>> getAllGrading(int page, int size) {
    page = page == 0 ? 0 : page - 1;
    Pageable pageable = PageRequest.of(page, size);
    Page<Grading> gradingPage = gradingRepository.findAllOrderByCreatedDateDesc(pageable);

    if (gradingPage.getContent().isEmpty())
      throw new ResourceNotFoundException("No Grading Records Found");

    return getPagedGradingResponse(page, gradingPage);
  }

  public ResponseEntity<PagedApiResponse<GradingResponse>> getStaffGrading(UUID staffId, int page, int size) {
    Staff staff = staffRepository.findById(staffId)
        .orElseThrow(() -> new ResourceNotFoundException("Staff with ID [ %s ] not found".formatted(staffId)));

    page = page == 0 ? 0 : page - 1;
    Pageable pageable = PageRequest.of(page, size);

    Page<Grading> staffGradingPage = gradingRepository.findByStaff(staff, pageable);

    if (staffGradingPage.getContent().isEmpty())
      throw new ResourceNotFoundException("No Grading Records Found for [ %s ]".formatted(staff.getFullName()));

    return getPagedGradingResponse(page, staffGradingPage);
  }

  @NotNull
  private ResponseEntity<PagedApiResponse<GradingResponse>> getPagedGradingResponse(int page, Page<Grading> staffGradingPage) {
    List<GradingResponse> gradingResponses = staffGradingPage.getContent().stream().map(INSTANCE::gradingToGradingResponse).toList();

    return ResponseEntity.ok(
        PagedApiResponse.<GradingResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Gradings Fetched Successfully")
            .data(
                gradingResponses
            )
            .pageNumber(page + 1)
            .totalPages(staffGradingPage.getTotalPages())
            .isLastPage(staffGradingPage.isLast())
            .build()
    );
  }

  @Transactional
  public ResponseEntity<ApiResponse<GradingResponse>> updateGrading(UUID gradingId, UpdateGradingRequest request) {
    Grading grading = gradingRepository.findById(gradingId)
        .orElseThrow(() -> new ResourceNotFoundException("Grading with ID [ %s ] does not exist"));

    grading.setGrade(request.newGrade());

    gradingRepository.save(grading);

    GradingResponse gradingResponse = INSTANCE.gradingToGradingResponse(grading);

    return ResponseEntity.ok(
        ApiResponse.<GradingResponse>builder()
            .statusCode(HttpStatus.OK.value())
            .status("Success")
            .message("Staff [ %s ] from [ %s ] Department Grade on [ %s ] Performance Updated to [ %s ] Successfully"
                .formatted(
                    grading.getStaff().getFullName(),
                    grading.getStaff().getDepartment().getName(),
                    grading.getPerformance().getName(),
                    request.newGrade()
                )
            )
            .data(
                gradingResponse
            )
            .build()
    );
  }
}

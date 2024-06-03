package com.sinnts.grading.grading;

import com.sinnts.grading.grading.dto.request.AddGradingRequest;
import com.sinnts.grading.grading.dto.request.UpdateGradingRequest;
import com.sinnts.grading.grading.dto.response.GradingResponse;
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
@RequestMapping("/gradings")
@RequiredArgsConstructor
@Tag(name = "Grading Controller")
@SecurityRequirement(name = "BearerAuth")
public class GradingController {

  private final GradingService gradingService;

  @PostMapping
  public ResponseEntity<ApiResponse<GradingResponse>> addGrading(
      @Valid @RequestBody AddGradingRequest request
  ) {
    return gradingService.addNewGrading(request);
  }

  @GetMapping("/all")
  public ResponseEntity<PagedApiResponse<GradingResponse>> getAllGrading(
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return gradingService.getAllGrading(page, size);
  }

  @GetMapping("/for-staff/{staffId}")
  public ResponseEntity<PagedApiResponse<GradingResponse>> getStaffGrading(
      @PathVariable UUID staffId,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return gradingService.getStaffGrading(staffId, page, size);
  }

  @PutMapping("/{gradingId}")
  public ResponseEntity<ApiResponse<GradingResponse>> updateGrading(
      @PathVariable UUID gradingId,
      @Valid @RequestBody UpdateGradingRequest request
  ) {
    return gradingService.updateGrading(gradingId, request);
  }
}

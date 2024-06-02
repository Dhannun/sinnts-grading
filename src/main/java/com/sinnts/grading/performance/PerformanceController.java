package com.sinnts.grading.performance;

import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.universal.PagedApiResponse;
import com.sinnts.grading.performance.dto.request.AddPerformanceRequest;
import com.sinnts.grading.performance.dto.request.UpdatePerformanceRequest;
import com.sinnts.grading.performance.dto.response.PerformanceResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/performances")
@RequiredArgsConstructor
@Tag(name = "Performance Controller")
public class PerformanceController {

  private final PerformanceService performanceService;

  @PostMapping
  public ResponseEntity<ApiResponse<PerformanceResponse>> createPerformance(
      @Valid @RequestBody AddPerformanceRequest request
  ) {
    return performanceService.createPerformance(request);
  }

  @GetMapping("/all")
  public ResponseEntity<PagedApiResponse<PerformanceResponse>> getAllPerformance(
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return performanceService.getAllPerformance(page, size);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<PerformanceResponse>> getPerformance(
      @RequestParam UUID performanceId
  ) {
    return performanceService.getPerformance(performanceId);
  }

  @PutMapping("/{performanceId}")
  public ResponseEntity<ApiResponse<PerformanceResponse>> updatePerformance(
      @PathVariable UUID performanceId,
      @RequestBody UpdatePerformanceRequest request
  ) {
    return performanceService.updatePerformance(performanceId, request);
  }

  @DeleteMapping("/{performanceId}")
  public ResponseEntity<ApiResponse<PerformanceResponse>> deletePerformance(
      @PathVariable UUID performanceId
  ) {
    return performanceService.deletePerformance(performanceId);
  }

  @PutMapping("/add-department/{performanceId}/{departmentId}")
  public ResponseEntity<ApiResponse<PerformanceResponse>> addDepartment(
      @PathVariable UUID performanceId,
      @PathVariable UUID departmentId
  ) {
    return performanceService.addDepartment(performanceId, departmentId);
  }

  @PutMapping("/remove-department/{performanceId}/{departmentId}")
  public ResponseEntity<ApiResponse<PerformanceResponse>> removeDepartment(
      @PathVariable UUID performanceId,
      @PathVariable UUID departmentId
  ) {
    return performanceService.removeDepartment(performanceId, departmentId);
  }

  @GetMapping("/for-department/{departmentId}")
  public ResponseEntity<PagedApiResponse<PerformanceResponse>> getDepartmentPerformances(
      @PathVariable UUID departmentId,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return performanceService.getDepartmentPerformance(departmentId, page, size);
  }
}

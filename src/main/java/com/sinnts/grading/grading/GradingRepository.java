package com.sinnts.grading.grading;

import com.sinnts.grading.staff.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface GradingRepository extends JpaRepository<Grading, UUID> {

  @Query(
      """
      SELECT g FROM Grading g ORDER BY g.createdDate DESC
      """
  )
  Page<Grading> findAllOrderByCreatedDateDesc(Pageable pageable);

  @Query(
      """
      SELECT g FROM Grading g WHERE g.staff = ?1 ORDER BY g.createdDate DESC
      """
  )
  Page<Grading> findByStaff(Staff staff, Pageable pageable);
}
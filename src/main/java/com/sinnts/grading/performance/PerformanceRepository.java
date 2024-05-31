package com.sinnts.grading.performance;

import com.sinnts.grading.department.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PerformanceRepository extends JpaRepository<Performance, UUID> {
  @Query(
      """
      SELECT p FROM Performance p ORDER BY p.createdDate DESC
      """
  )
  Page<Performance> findAllOrderByCreatedDateDesc(Pageable pageable);

  @Query(
      """
      select p from Performance p inner join p.departments departments where departments.id = ?1 order by p.createdDate desc
      """
  )
  Page<Performance> findByDepartments_Id(UUID id, Pageable pageable);
}
package com.sinnts.grading.department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

  @Query(
      """
      SELECT d FROM Department d ORDER BY d.createdDate DESC
      """
  )
  Page<Department> findAllOrderByCreatedDateDesc(Pageable pageable);
}
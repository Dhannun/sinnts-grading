package com.sinnts.grading.staff;

import com.sinnts.grading.department.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface StaffRepository extends JpaRepository<Staff, UUID> {
  boolean existsByIdentity(String identity);

  @Query(
      """
      SELECT s FROM Staff s ORDER BY s.createdDate DESC
      """
  )
  Page<Staff> findAllOrderByCreatedDateDesc(Pageable pageable);

  @Query(
      """
      SELECT s FROM Staff s WHERE s.department = :department ORDER BY s.createdDate DESC
      """
  )
  Page<Staff> getStaffsByDepartment(Department department, Pageable pageable);
}
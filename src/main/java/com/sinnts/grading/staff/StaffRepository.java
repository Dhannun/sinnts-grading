package com.sinnts.grading.staff;

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
}
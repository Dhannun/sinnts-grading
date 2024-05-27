package com.sinnts.grading.performance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PerformanceRepository extends JpaRepository<Performance, UUID> {
}
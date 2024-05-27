package com.sinnts.grading.grading;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GradingRepository extends JpaRepository<Grading, UUID> {
}
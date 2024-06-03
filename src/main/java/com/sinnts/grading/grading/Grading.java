package com.sinnts.grading.grading;

import com.sinnts.grading.grading.enums.Grade;
import com.sinnts.grading.performance.Performance;
import com.sinnts.grading.staff.Staff;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "sinnts_staff_grading")
@EntityListeners(AuditingEntityListener.class)
public class Grading {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;

  @OneToOne(orphanRemoval = true)
  @JoinColumn(name = "performance_id")
  private Performance performance;

  @Enumerated(EnumType.STRING)
  @Column(name = "grade")
  private Grade grade;

  @CreatedDate
  @Column(name = "created_date", nullable = false, updatable = false)
  private LocalDateTime createdDate;

  @LastModifiedDate
  @Column(name = "last_modified_date", insertable = false)
  private LocalDateTime lastModifiedDate;

  @CreatedBy
  @Column(name = "created_by", nullable = false, updatable = false)
  private UUID createdBy;

  @LastModifiedDate
  @Column(name = "last_modified_by", insertable = false)
  private UUID lastModifiedBy;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "staff_id")
  private Staff staff;

}

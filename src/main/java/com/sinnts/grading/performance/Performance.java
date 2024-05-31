package com.sinnts.grading.performance;

import com.sinnts.grading.department.Department;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "sinnts_staff_performance")
@EntityListeners(AuditingEntityListener.class)
public class Performance {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "name")
  private String name;

  @CreatedDate
  @Column(name = "created_date", nullable = false, updatable = false)
  private LocalDateTime createdDate;

  @LastModifiedDate
  @Column(name = "last_modified_date", insertable = false)
  private LocalDateTime lastModifiedDate;

  @ManyToMany(mappedBy = "performances")
  private Set<Department> departments = new LinkedHashSet<>();

}

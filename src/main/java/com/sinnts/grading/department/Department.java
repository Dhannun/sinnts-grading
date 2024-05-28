package com.sinnts.grading.department;

import com.sinnts.grading.performance.Performance;
import com.sinnts.grading.staff.Staff;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "sinnts_department")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
public class Department {
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

  @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Staff> staffs = new LinkedHashSet<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Performance> performances = new LinkedHashSet<>();

}

package com.sinnts.grading.department;

import com.sinnts.grading.performance.Performance;
import com.sinnts.grading.staff.Staff;
import com.sinnts.grading.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
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

  @ManyToOne
  @JoinColumn(name = "created_by", nullable = false, updatable = false)
  @CreatedBy
  private User createdBy;

  @ManyToOne
  @JoinColumn(name = "last_modified_by", insertable = false)
  @LastModifiedBy
  private User lastModifiedBy;

//  @CreatedBy
//  @Column(name = "created_by", nullable = false, updatable = false)
//  private UUID createdBy;
//
//  @LastModifiedBy
//  @Column(name = "last_modified_by", insertable = false)
//  private UUID lastModifiedBy;

  @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<Staff> staffs = new LinkedHashSet<>();

  @ManyToMany
  @JoinTable(name = "sinnts_department_performances",
      joinColumns = @JoinColumn(name = "department_id"),
      inverseJoinColumns = @JoinColumn(name = "performances_id"))
  @Builder.Default
  private Set<Performance> performances = new LinkedHashSet<>();

}

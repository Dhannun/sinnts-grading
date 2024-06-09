package com.sinnts.grading.staff;

import com.sinnts.grading.department.Department;
import com.sinnts.grading.grading.Grading;
import com.sinnts.grading.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "sinnts_staff")
@EntityListeners(AuditingEntityListener.class)
public class Staff {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "title")
  private String title;

  @Column(name = "identity")
  private String identity;

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

  @ManyToOne
  @JoinColumn(name = "department_id", nullable = false)
  private Department department;

  @OneToMany(mappedBy = "staff", orphanRemoval = true)
  @Builder.Default
  private Set<Grading> gradings = new LinkedHashSet<>();

}

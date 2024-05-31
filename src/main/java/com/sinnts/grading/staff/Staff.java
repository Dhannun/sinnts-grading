package com.sinnts.grading.staff;

import com.sinnts.grading.department.Department;
import com.sinnts.grading.grading.Grading;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
  @JoinColumn(name = "department_id", nullable = false)
  private Department department;

  @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
  @JoinColumn(name = "staff_id")
  private List<Grading> gradings = new ArrayList<>();
}

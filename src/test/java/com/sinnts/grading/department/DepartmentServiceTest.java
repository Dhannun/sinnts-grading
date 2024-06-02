package com.sinnts.grading.department;

import com.sinnts.grading.department.dto.request.AddDepartmentRequest;
import com.sinnts.grading.department.dto.response.DepartmentResponse;
import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.universal.PagedApiResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DepartmentServiceTest {

  // Auto-inject the required details [username, password, port, ...]
  @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

  static {
    postgreSQLContainer.start();
  }

  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private DepartmentService departmentService;

  private Department department;

  @BeforeAll
  void setUp() {
    department = Department.builder()
        .id(UUID.randomUUID())
        .name("Test Department")
        .build();
    departmentRepository.saveAndFlush(department);
  }

  @Test
  void addDepartment() {
    AddDepartmentRequest request = new AddDepartmentRequest("Test Department");

    ResponseEntity<ApiResponse<DepartmentResponse>> response = departmentService.addDepartment(request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", Objects.requireNonNull(response.getBody()).getStatus());
    assertEquals("Department Created successfully", response.getBody().getMessage());
    assertEquals(request.name(), response.getBody().getData().name());

  }

  @Test
  void testGetAllDepartments() {
    ResponseEntity<PagedApiResponse<DepartmentResponse>> response = departmentService.getAllDepartments(1, 10);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", Objects.requireNonNull(response.getBody()).getStatus());
    assertEquals(1, response.getBody().getTotalPages());
    assertEquals(1, response.getBody().getPageNumber());
  }

  @Test
  void updateDepartmentName() {
  }

/*  @Test
  void getOneDepartmentById() {
    Department foundDepartment = departmentService.getDepartmentByID(department.getId());

    assertNotNull(foundDepartment);
    assertEquals(department.getName(), foundDepartment.getName());
  }*/
}
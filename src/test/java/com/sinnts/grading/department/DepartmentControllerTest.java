package com.sinnts.grading.department;

import com.sinnts.grading.department.dto.response.DepartmentResponse;
import com.sinnts.grading.universal.ApiResponse;
import com.sinnts.grading.universal.PagedApiResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;
import org.assertj.core.api.AssertionsForClassTypes;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DepartmentControllerTest {

  // Auto inject the required details [username, password, port, ...]
  @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

  @LocalServerPort
  private Integer port;

  @Autowired
  TestRestTemplate restTemplate;

  static {
    postgreSQLContainer.start();
  }

  @Test
  void shouldAddDepartment() {
  }

  @Test
  void getAllDepartments() {
  }

  @Test
  void getDepartmentById() {
  }

  @Test
  void updateDepartmentName() {
  }

  @Test
  void deleteDepartmentName() {
  }
}
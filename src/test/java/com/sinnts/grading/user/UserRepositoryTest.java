package com.sinnts.grading.user;

import com.sinnts.grading.config.auditing.ApplicationAuditAware;
import com.sinnts.grading.config.auditing.AuditAwareBean;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static com.sinnts.grading.user.enums.Role.ADMIN;
import static com.sinnts.grading.user.enums.Role.SUPER_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ApplicationAuditAware.class, AuditAwareBean.class}) // Import the necessary configuration
class UserRepositoryTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.3"));

  @Autowired
  UserRepository underTest;

  private User superAdmin;

  @BeforeAll
  static void beforeAll() {
    Flyway flyway = Flyway.configure()
        .dataSource(
            postgreSQLContainer.getJdbcUrl(),
            postgreSQLContainer.getUsername(),
            postgreSQLContainer.getPassword()
        )
        .load();
    flyway.migrate();
  }

  @BeforeEach
  void setUp() {
    if (underTest.count() == 0) {
      superAdmin = User.builder()
          .username("superadmin")
          .password("$2a$10$LYdFkceJtNTUvO/69/BKBOQaoi5ki2ZsR8Fu0VmbXTbku8HJg45ti")
          .role(SUPER_ADMIN)
          .fullName("Super Admin")
          .enabled(true)
          .locked(false)
          .build();

      superAdmin.setCreatedBy(superAdmin);
      underTest.save(superAdmin);
    }
  }

  @Test
  void canEstablishConnection() {
    assertThat(postgreSQLContainer.isCreated()).isTrue();
    assertThat(postgreSQLContainer.isRunning()).isTrue();
  }

  @Test
  void shouldGetSuperAdmin() {
    // given
    // when
    Optional<User> superAdmin = underTest.getSuperAdmin();
    // then
    assertThat(superAdmin.isPresent()).isTrue();
    assertThat(superAdmin.get().getUsername()).isEqualTo("superadmin");
    assertThat(superAdmin.get().getRole()).isEqualTo(SUPER_ADMIN);
    assertThat(superAdmin.get().getFullName()).isEqualTo("Super Admin");
    assertThat(superAdmin.get().getCreatedBy()).isNotNull();
  }

  @Test
  void shouldBeTrueIfExistsByUsernameIgnoreCase() {
    // given
    String validUsername = "SupeRAdmiN";
    // when
    boolean existsByUsernameIgnoreCase = underTest.existsByUsernameIgnoreCase(validUsername);
    // then
    assertThat(existsByUsernameIgnoreCase).isTrue();
  }

  @Test
  void shouldBeFalseIfNotExistsByUsernameIgnoreCase() {
    // given
    String invalidUsername = "FakeSupeRAdmiN";
    // when
    boolean existsByUsernameIgnoreCase = underTest.existsByUsernameIgnoreCase(invalidUsername);
    // then
    assertThat(existsByUsernameIgnoreCase).isFalse();
  }

  @Test
  void shouldFindUserByUsernameIgnoreCaseWhenIsPresent() {

    // given
    String username = "admin";
    User user = User.builder()
        .username(username)
        .password("$2a$10$LYdFkceJtNTUvO/69/BKBOQaoi5ki2ZsR8Fu0VmbXTbku8HJg45ti")
        .role(ADMIN)
        .fullName("Normal Admin")
        .createdBy(superAdmin)
        .enabled(true)
        .locked(false)
        .build();
    underTest.save(user);

    // when
    Optional<User> userByUsernameIgnoreCase = underTest.findByUsernameIgnoreCase(username);

    // then
    assertThat(userByUsernameIgnoreCase).isPresent();
    assertThat(userByUsernameIgnoreCase.get().getUsername()).isEqualTo(username);
    assertThat(userByUsernameIgnoreCase.get().getFullName()).isEqualTo("Normal Admin");
    assertThat(userByUsernameIgnoreCase.get().getRole()).isEqualTo(ADMIN);
    assertThat(userByUsernameIgnoreCase.get().getCreatedBy()).isEqualTo(superAdmin);
  }

  @Test
  void shouldNotFindUserByUsernameIgnoreCaseWhenIsNotPresent() {

    // given
    String username = "admin";
    User user = User.builder()
        .username(username)
        .password("$2a$10$LYdFkceJtNTUvO/69/BKBOQaoi5ki2ZsR8Fu0VmbXTbku8HJg45ti")
        .role(ADMIN)
        .fullName("Normal Admin")
        .createdBy(superAdmin)
        .enabled(true)
        .locked(false)
        .build();
    underTest.save(user);

    // when
    Optional<User> userByUsernameIgnoreCase = underTest.findByUsernameIgnoreCase("fakeadmin");

    // then
    assertThat(userByUsernameIgnoreCase).isNotPresent();
  }
}
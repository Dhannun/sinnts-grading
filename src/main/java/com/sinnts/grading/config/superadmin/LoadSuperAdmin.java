package com.sinnts.grading.config.superadmin;

import com.sinnts.grading.user.User;
import com.sinnts.grading.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.sinnts.grading.user.enums.Role.SUPER_ADMIN;

@RequiredArgsConstructor
@Component
@Slf4j
public class LoadSuperAdmin implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    Optional<User> superAdminOptional = userRepository.getSuperAdmin();

    if (superAdminOptional.isEmpty()) {
      log.info("No Super Admin Found, Creating One...");
      final UUID id = UUID.randomUUID();
      User superAdmin = User.builder()
          .id(id)
          .username("superadmin")
          .password(passwordEncoder.encode("superadmin"))
          .role(SUPER_ADMIN)
          .fullName("Super Admin")
          .enabled(true)
          .locked(false)
          .build();

      superAdmin.setCreatedBy(superAdmin);

      userRepository.save(superAdmin);
      log.info("Super Admin Created Successfully ✨");
    }
    log.info("API Ready to Accept Requests 🚀");
  }
}

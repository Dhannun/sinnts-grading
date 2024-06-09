package com.sinnts.grading.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
  @Query("select (count(u) > 0) from User u where upper(u.username) = upper(?1)")
  boolean existsByUsernameIgnoreCase(String username);

  @Query("select u from User u where upper(u.username) = upper(?1)")
  Optional<User> findByUsernameIgnoreCase(String username);

  @Query("select u from User u where u.role = 'SUPER_ADMIN'")
  Optional<User> getSuperAdmin();
}

package com.sinnts.grading.jwt;

import com.sinnts.grading.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {

    @Query("SELECT t FROM Token t WHERE t.user = :user AND t.expired = false AND t.revoked = false")
    List<Token> findAllValidTokenByUser(User user);

    Optional<Token> findByToken(String token);
}

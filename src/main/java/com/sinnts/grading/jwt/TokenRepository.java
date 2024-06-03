package com.sinnts.grading.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {

//    SELECT t FROM Token t INNER JOIN AppUser u ON t.app_user.id = u.id
//    WHERE u.id = :userId AND (t.expired = false OR t.revoked = false)
    @Query("SELECT t FROM Token t WHERE t.user.id = :userId AND t.expired = false AND t.revoked = false")
    List<Token> findAllValidTokenByUser(Long userId);

    Optional<Token> findByToken(String token);
}

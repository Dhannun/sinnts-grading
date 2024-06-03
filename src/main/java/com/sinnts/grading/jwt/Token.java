package com.sinnts.grading.jwt;


import com.sinnts.grading.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sinnts_access_tokens")
@Setter
@Getter
public class Token {

    @Id
    private UUID id;
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    private boolean expired;
    private boolean revoked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID();
    }
}

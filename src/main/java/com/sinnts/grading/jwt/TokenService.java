package com.sinnts.grading.jwt;

import com.sinnts.grading.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;

  public List<Token> findAllValidTokenByUser(User user) {
    return tokenRepository.findAllValidTokenByUser(user);
  }

  public void saveAllTokens(List<Token> validTokens) {
    tokenRepository.saveAll(validTokens);
  }

  public void saveToken(Token token) {
    tokenRepository.save(token);
  }
}

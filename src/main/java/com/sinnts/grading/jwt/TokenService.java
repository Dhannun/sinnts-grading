package com.sinnts.grading.jwt;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;

  public List<Token> findAllValidTokenByUser(Long id) {
    return tokenRepository.findAllValidTokenByUser(id);
  }

  public void saveAllTokens(List<Token> validTokens) {
    tokenRepository.saveAll(validTokens);
  }

  public void saveToken(Token token) {
    tokenRepository.save(token);
  }
}

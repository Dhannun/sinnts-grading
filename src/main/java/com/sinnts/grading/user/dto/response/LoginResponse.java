package com.sinnts.grading.user.dto.response;

public record LoginResponse(
    String accessToken,
    long accessTokenExpiresInMinutes,
    String refreshToken,
    long refreshTokenExpiresInMinutes
) {
}

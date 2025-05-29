package goorm.deepdive.team1.infra.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import goorm.deepdive.team1.domain.admin.infrastructure.TokenRepository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:"; // Redis Key Prefix 설정

    //  Redis에 Refresh Token 저장
    @Override
    public void saveRefreshToken(String adminId, String refreshToken, long expirationSeconds) {
        String key = REFRESH_TOKEN_PREFIX + adminId;
        redisTemplate.opsForValue().set(key, refreshToken, expirationSeconds, TimeUnit.SECONDS);
    }

    //  Redis에서 Refresh Token 조회
    @Override
    public String getRefreshToken(String adminId) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + adminId);
    }

    //  Redis에서 Refresh Token 삭제
    public void deleteRefreshToken(String adminId) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + adminId);
    }
}
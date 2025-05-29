package goorm.deepdive.team1.domain.admin.infrastructure;

public interface TokenRepository  {
    void saveRefreshToken(String adminId, String refreshToken, long expirationSeconds);
    String getRefreshToken(String adminId);
    void deleteRefreshToken(String adminId);
}
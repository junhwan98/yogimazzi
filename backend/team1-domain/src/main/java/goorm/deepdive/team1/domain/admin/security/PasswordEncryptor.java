package goorm.deepdive.team1.domain.admin.security;

public interface PasswordEncryptor {
    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
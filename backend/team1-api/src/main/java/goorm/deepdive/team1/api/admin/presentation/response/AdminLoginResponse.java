package goorm.deepdive.team1.api.admin.presentation.response;

import lombok.Getter;

public record AdminLoginResponse(
        Long id,
        String email,
        String token
) {
    public static AdminLoginResponse from(Long id, String email, String token) {
        return new AdminLoginResponse(id, email, token);
    }
}
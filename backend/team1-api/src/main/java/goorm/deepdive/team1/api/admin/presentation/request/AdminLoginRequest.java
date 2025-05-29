package goorm.deepdive.team1.api.admin.presentation.request;

public record AdminLoginRequest(
        String email,
        String password
) {
}
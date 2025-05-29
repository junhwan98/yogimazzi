package goorm.deepdive.team1.api.admin.presentation.response;

import goorm.deepdive.team1.domain.admin.domain.Admin;

public record AdminSearchResponse(
        Long id,
        String email,
        String role
) {
    public static AdminSearchResponse from(Admin admin) {
        return new AdminSearchResponse(admin.getId(), admin.getEmail(), admin.getRole().name());
    }
}
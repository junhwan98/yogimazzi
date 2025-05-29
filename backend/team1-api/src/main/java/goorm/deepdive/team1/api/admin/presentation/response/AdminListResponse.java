package goorm.deepdive.team1.api.admin.presentation.response;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.domain.Role;
import lombok.Builder;

@Builder
public record AdminListResponse(
        Long id,
        String email,
        Role role
) {
    public static AdminListResponse from(Admin admin) {
        return AdminListResponse.builder()
            .id(admin.getId())
            .email(admin.getEmail())
            .role(admin.getRole())
            .build();
    }
}
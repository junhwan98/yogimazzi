package goorm.deepdive.team1.api.admin.presentation.response;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import lombok.Builder;

@Builder
public record AdminRegisterResponse(
        Long id
) {
    public static AdminRegisterResponse from(Admin admin) {
        return AdminRegisterResponse.builder()
            .id(admin.getId())
            .build();
    }
}
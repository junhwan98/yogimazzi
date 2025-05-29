package goorm.deepdive.team1.api.admin.presentation.request;

import goorm.deepdive.team1.domain.admin.domain.Role;
import jakarta.validation.constraints.NotNull;

public record AdminRoleUpdateRequest(
        @NotNull(message = "변경할 권한을 입력해주세요.") Role role
) {
}
package goorm.deepdive.team1.api.admin.presentation.request;

import goorm.deepdive.team1.domain.admin.domain.Role;

public record AdminRegisterRequest(
        String email,
        String password,
        Role role
) {
}
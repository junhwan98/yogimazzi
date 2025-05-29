package goorm.deepdive.team1.api.admin.presentation.request;

public record AdminPasswordUpdateRequest(
        String oldPassword,
        String newPassword
){
}
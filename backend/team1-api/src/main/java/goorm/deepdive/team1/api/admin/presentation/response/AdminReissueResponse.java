package goorm.deepdive.team1.api.admin.presentation.response;

public record AdminReissueResponse(
        Long id,
        String token
) {
    public static AdminReissueResponse from(Long id, String token) {
        return new AdminReissueResponse(id, token);
    }
}
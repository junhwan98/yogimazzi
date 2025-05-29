package goorm.deepdive.team1.api.user.presentation.resonse;

import java.util.List;

import lombok.Builder;

@Builder
public record UserHeatMapListResponse(
	List<UserHeatMapResponse> response
) {
	public static UserHeatMapListResponse of(List<UserHeatMapResponse> response) {
		return UserHeatMapListResponse.builder()
			.response(response)
			.build();
	}
}

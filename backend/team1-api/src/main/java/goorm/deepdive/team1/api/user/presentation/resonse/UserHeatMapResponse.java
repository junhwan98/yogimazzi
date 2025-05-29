package goorm.deepdive.team1.api.user.presentation.resonse;

import goorm.deepdive.team1.domain.user.domain.UserDocument;
import lombok.Builder;

@Builder
public record UserHeatMapResponse(
	double x,
	double y,
	String region
) {
	public static UserHeatMapResponse from(UserDocument userDocument){
		return UserHeatMapResponse.builder()
			.x(userDocument.getX())
			.y(userDocument.getY())
			.region(userDocument.getRegion())
			.build();
	}
}

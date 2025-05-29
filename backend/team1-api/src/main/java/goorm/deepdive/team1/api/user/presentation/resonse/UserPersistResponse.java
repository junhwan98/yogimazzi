package goorm.deepdive.team1.api.user.presentation.resonse;

import goorm.deepdive.team1.domain.user.domain.User;
import lombok.Builder;

@Builder
public record UserPersistResponse(
	Long id
) {
	public static UserPersistResponse from(User user) {
		return UserPersistResponse.builder()
			.id(user.getId())
			.build();
	}
}

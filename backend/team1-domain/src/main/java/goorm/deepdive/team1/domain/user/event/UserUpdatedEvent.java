package goorm.deepdive.team1.domain.user.event;

import goorm.deepdive.team1.domain.user.domain.User;
import lombok.Builder;

@Builder
public record UserUpdatedEvent(
	User user
) {
	public static UserUpdatedEvent of(User user) {
		return UserUpdatedEvent.builder()
			.user(user)
			.build();
	}
}

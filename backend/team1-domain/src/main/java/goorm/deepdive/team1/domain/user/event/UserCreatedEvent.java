package goorm.deepdive.team1.domain.user.event;

import goorm.deepdive.team1.domain.user.domain.User;
import lombok.Builder;

@Builder
public record UserCreatedEvent(
	User user
) {
	public static UserCreatedEvent of(User user) {
		return UserCreatedEvent.builder()
			.user(user)
			.build();
	}
}

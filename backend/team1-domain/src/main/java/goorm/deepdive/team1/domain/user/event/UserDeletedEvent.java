package goorm.deepdive.team1.domain.user.event;

import java.util.List;

import lombok.Builder;

@Builder
public record UserDeletedEvent(
	List<Long> userIds
) {
	public static UserDeletedEvent of(List<Long> userIds) {
		return UserDeletedEvent.builder()
			.userIds(userIds)
			.build();
	}
}

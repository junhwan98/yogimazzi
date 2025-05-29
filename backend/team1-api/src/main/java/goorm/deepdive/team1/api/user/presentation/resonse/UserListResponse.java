package goorm.deepdive.team1.api.user.presentation.resonse;

import java.util.List;

import goorm.deepdive.team1.domain.user.domain.User;
import lombok.Builder;

@Builder
public record UserListResponse(
	List<UserResponse> users
) {
	public static UserListResponse from(List<User> userList) {
		return UserListResponse.builder()
			.users(userList.stream().map(UserResponse::from).toList())
			.build();
	}
}

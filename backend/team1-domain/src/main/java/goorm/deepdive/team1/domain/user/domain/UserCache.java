package goorm.deepdive.team1.domain.user.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@RedisHash(value = "users", timeToLive = 60 * 60 * 24)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCache {
	@Id
	private Long id;

	@Indexed
	private String name;

	private String email;

	private String phoneNumber;

	private String latestRegionAddress;

	private String latestRoadAddress;

	private String gender;

	private Integer age;

	public static UserCache create(Long id, String name, String email, String phoneNumber, String latestRegionAddress, String latestRoadAddress, String gender, Integer age) {
		return UserCache.builder()
				.id(id)
				.name(name)
				.email(email)
				.phoneNumber(phoneNumber)
				.latestRegionAddress(latestRegionAddress)
				.latestRoadAddress(latestRoadAddress)
				.gender(gender)
				.age(age)
				.build();
	}

	public static UserCache from(User user) {
		return UserCache.builder()
			.id(user.getId())
			.name(user.getName())
			.email(user.getEmail())
			.phoneNumber(user.getPhoneNumber())
			.latestRegionAddress(user.getAddress().getRegionAddress())
			.latestRoadAddress(user.getAddress().getRoadAddress())
			.gender(user.getGender().getValue())
			.age(user.getAge())
			.build();
	}
}

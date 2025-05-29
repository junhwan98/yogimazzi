package goorm.deepdive.team1.domain.addresshistory.domain;

import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@RedisHash(value = "addressHistories", timeToLive = 60 * 60 * 24)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressHistoryCache {
	@Id
	private Long id;

	@Indexed
	private Long userId;

	private String regionAddress;
	private String roadAddress;

	private String createdAt;

	public static AddressHistoryCache create(Long id, Long userId, String regionAddress, String roadAddress, String createdAt) {
		return AddressHistoryCache.builder()
			.id(id)
			.userId(userId)
			.regionAddress(regionAddress)
			.roadAddress(roadAddress)
			.createdAt(createdAt)
			.build();
	}

	public static AddressHistoryCache from(AddressHistory addressHistory) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
		String createdAt = addressHistory.getCreatedAt().format(formatter);
		return AddressHistoryCache.builder()
			.id(addressHistory.getId())
			.userId(addressHistory.getUser().getId())
			.regionAddress(addressHistory.getAddress().getRegionAddress())
			.roadAddress(addressHistory.getAddress().getRoadAddress())
			.createdAt(createdAt)
			.build();
	}
}

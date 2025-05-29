package goorm.deepdive.team1.api.addresshistory.presentation.response;

import java.time.format.DateTimeFormatter;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;
import lombok.Builder;

@Builder
public record AddressHistoryResponse (
	String roadAddress,
	String regionAddress,
	String createdAt
) {
	public static AddressHistoryResponse from(AddressHistoryCache addressHistoryCache) {
		return AddressHistoryResponse.builder()
			.roadAddress(addressHistoryCache.getRoadAddress())
			.regionAddress(addressHistoryCache.getRegionAddress())
			.createdAt(addressHistoryCache.getCreatedAt())
			.build();
	}

	public static AddressHistoryResponse from(AddressHistory addressHistory) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
		String createdAt = addressHistory.getCreatedAt().format(formatter);
		Address address = addressHistory.getAddress();
		return AddressHistoryResponse.builder()
			.roadAddress(address.getRoadAddress())
			.regionAddress(address.getRegionAddress())
			.createdAt(createdAt)
			.build();
	}
}

package goorm.deepdive.team1.api.addresshistory.presentation.response;

import java.util.List;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;
import lombok.Builder;

@Builder
public record AddressHistoryListResponse (
	List<AddressHistoryResponse> responses
) {
	public static AddressHistoryListResponse fromCaches(List<AddressHistoryCache> addressHistoryCaches) {
			List<AddressHistoryResponse> responses = addressHistoryCaches.stream()
				.map(AddressHistoryResponse::from)
				.toList();

			return AddressHistoryListResponse.builder()
				.responses(responses)
				.build();
	}

	public static AddressHistoryListResponse fromEntity(List<AddressHistory> addressHistories) {
		List<AddressHistoryResponse> responses = addressHistories.stream()
			.map(AddressHistoryResponse::from)
			.toList();

		return AddressHistoryListResponse.builder()
			.responses(responses)
			.build();
	}
}

package goorm.deepdive.team1.api.addresshistory.Application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import goorm.deepdive.team1.api.addresshistory.presentation.response.AddressHistoryListResponse;
import goorm.deepdive.team1.domain.addresshistory.application.AddressHistoryCommandService;
import goorm.deepdive.team1.domain.addresshistory.application.AddressHistoryQueryService;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressHistoryFacade {
	private final AddressHistoryQueryService addressHistoryQueryService;
	private final AddressHistoryCommandService addressHistoryCommandService;

	@Transactional(readOnly = true)
	public AddressHistoryListResponse getAddressHistories(Long userId) {
		List<AddressHistoryCache> cachedHistories = addressHistoryQueryService.getAddressHistoryCaches(userId);

		if (!CollectionUtils.isEmpty(cachedHistories)) {
			return AddressHistoryListResponse.fromCaches(cachedHistories);
		}

		List<AddressHistory> addressHistories = addressHistoryQueryService.getByUserIdDeletedAtIsNull(userId);

		if (!addressHistories.isEmpty()) {
			addressHistoryCommandService.saveAll(addressHistories);
		}

		return AddressHistoryListResponse.fromEntity(addressHistories);
	}
}

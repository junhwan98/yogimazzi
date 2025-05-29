package goorm.deepdive.team1.domain.addresshistory.application;

import java.util.List;

import org.springframework.stereotype.Service;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;
import goorm.deepdive.team1.domain.addresshistory.infrastructure.AddressHistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressHistoryQueryService {
	private final AddressHistoryRepository addressHistoryRepository;

	public List<AddressHistory> getByUserIdDeletedAtIsNull(Long userId) {
		return addressHistoryRepository.findByUserIdAndDeletedAtIsNull(userId);
	}

	public List<AddressHistoryCache> getAddressHistoryCaches(Long userId) {
		return addressHistoryRepository.findCacheByUserIdAndDeletedAtIsNull(userId);
	}
}

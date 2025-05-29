package goorm.deepdive.team1.domain.addresshistory.infrastructure;

import java.util.List;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;

public interface AddressHistoryRepository {
	AddressHistory save(AddressHistory addressHistory);

	List<AddressHistory> findByUserIdAndDeletedAtIsNull(Long userId);

	List<AddressHistoryCache> findCacheByUserIdAndDeletedAtIsNull(Long userId);

	void saveAllCaches(List<AddressHistory> addressHistories);

	void deleteScheduling(List<Long> userIds);
}

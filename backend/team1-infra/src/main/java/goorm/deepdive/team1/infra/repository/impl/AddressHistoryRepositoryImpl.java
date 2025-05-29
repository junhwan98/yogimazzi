package goorm.deepdive.team1.infra.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;
import goorm.deepdive.team1.domain.addresshistory.infrastructure.AddressHistoryRepository;
import goorm.deepdive.team1.infra.repository.jpa.JpaAddressHistoryRepository;
import goorm.deepdive.team1.infra.repository.redis.RedisAddressHistoryRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AddressHistoryRepositoryImpl implements AddressHistoryRepository {
	private final JpaAddressHistoryRepository jpaAddressHistoryRepository;
	private final RedisAddressHistoryRepository redisAddressHistoryRepository;

	@Override
	public AddressHistory save(AddressHistory addressHistory) {
		return jpaAddressHistoryRepository.save(addressHistory);
	}

	@Override
	public List<AddressHistory> findByUserIdAndDeletedAtIsNull(Long userId) {
		return jpaAddressHistoryRepository.findByUserIdAndDeletedAtIsNull(userId);
	}

	@Override
	public List<AddressHistoryCache> findCacheByUserIdAndDeletedAtIsNull(Long userId) {
		return redisAddressHistoryRepository.findByUserId(userId);
	}

	@Override
	public void saveAllCaches(List<AddressHistory> addressHistories) {
		redisAddressHistoryRepository.saveAll(addressHistories);
	}

	@Override
	public void deleteScheduling(List<Long> userIds) {
		jpaAddressHistoryRepository.deleteScheduling(userIds);
		redisAddressHistoryRepository.deleteByUserIds(userIds);
	}
}

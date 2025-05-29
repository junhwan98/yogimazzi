package goorm.deepdive.team1.infra.repository.redis;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisAddressHistoryRepository {
	private static final String KEY_PREFIX = "addressHistories:";

	private final RedisTemplate<String, Object> redisTemplate;

	public void save(AddressHistoryCache addressHistoryCache) {
		String key = KEY_PREFIX + addressHistoryCache.getUserId() + ":" + addressHistoryCache.getId();
		redisTemplate.opsForValue().set(key, addressHistoryCache);
	}

	public void saveAll(List<AddressHistory> addressHistories) {
		if (addressHistories == null || addressHistories.isEmpty()) {
			return;
		}

		Map<String, AddressHistoryCache> cacheMap = addressHistories.stream()
			.map(AddressHistoryCache::from)
			.collect(Collectors.toMap(
				cache -> KEY_PREFIX + cache.getUserId() + ":" + cache.getId(),
				cache -> cache
			));

		redisTemplate.opsForValue().multiSet(cacheMap);
	}

	public List<AddressHistoryCache> findByUserId(Long userId) {
		String pattern = KEY_PREFIX + userId + ":*";
		return redisTemplate.keys(pattern).stream()
			.map(key -> (AddressHistoryCache)redisTemplate.opsForValue().get(key))
			.filter(Objects::nonNull)
			.sorted(Comparator.comparing(AddressHistoryCache::getCreatedAt).reversed())
			.toList();
	}

	public void deleteByUserId(Long userId) {
		String pattern = KEY_PREFIX + userId + ":*";
		redisTemplate.delete(redisTemplate.keys(pattern));
	}

	public void deleteByUserIds(List<Long> userIds) {
		List<String> keysToDelete = userIds.stream()
			.flatMap(userId -> Objects.requireNonNull(redisTemplate.keys(KEY_PREFIX + userId + ":*")).stream())
			.toList();

		if (!keysToDelete.isEmpty()) {
			redisTemplate.delete(keysToDelete);
		}
	}
}

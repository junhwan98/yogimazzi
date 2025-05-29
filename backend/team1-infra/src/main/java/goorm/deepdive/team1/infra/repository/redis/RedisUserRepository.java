package goorm.deepdive.team1.infra.repository.redis;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import goorm.deepdive.team1.domain.user.domain.UserCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisUserRepository {
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	private static final String USER_KEY_PREFIX = "users:";
	private static final String USER_ZSET_KEY = "user:zset";

	public Page<UserCache> findAllSorted(Pageable pageable) {
		long start = pageable.getOffset();
		long end = start + pageable.getPageSize() - 1;

		Set<String> userIds = redisTemplate.opsForZSet().range(USER_ZSET_KEY, start, end);
		if (userIds == null || userIds.isEmpty()) {
			return Page.empty();
		}

		List<String> userJsonList = redisTemplate.opsForValue().multiGet(
			userIds.stream().map(id -> USER_KEY_PREFIX + id).collect(Collectors.toList())
		);

		List<UserCache> users = userJsonList.stream()
			.filter(Objects::nonNull)
			.map(this::deserializeUserCache)
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		Long totalElements = redisTemplate.opsForZSet().size(USER_ZSET_KEY);
		return new PageImpl<>(users, pageable, totalElements != null ? totalElements : 0);
	}

	public UserCache findById(Long id) {
		String jsonValue = redisTemplate.opsForValue().get(USER_KEY_PREFIX + id);
		return deserializeUserCache(jsonValue);
	}

	public void save(UserCache userCache) {
		String jsonValue = serializeUserCache(userCache);
		if (jsonValue != null) {
			redisTemplate.opsForValue().set(USER_KEY_PREFIX + userCache.getId(), jsonValue);
			redisTemplate.opsForZSet().add(USER_ZSET_KEY, userCache.getId().toString(), userCache.getId());
		}
	}

	public void saveAll(List<UserCache> userCaches) {
		if (userCaches == null || userCaches.isEmpty()) {
			return;
		}

		Map<String, String> cacheMap = userCaches.stream()
			.collect(Collectors.toMap(
				userCache -> USER_KEY_PREFIX + userCache.getId(),
				this::serializeUserCache
			));

		redisTemplate.opsForValue().multiSet(cacheMap);

		Set<ZSetOperations.TypedTuple<String>> userIdSet = userCaches.stream()
			.map(userCache -> new DefaultTypedTuple<>(userCache.getId().toString(), (double) userCache.getId()))
			.collect(Collectors.toSet());

		redisTemplate.opsForZSet().add(USER_ZSET_KEY, userIdSet);
	}

	public void deleteByIds(List<Long> ids) {
		List<String> keysToDelete = ids.stream()
			.map(id -> USER_KEY_PREFIX + id)
			.collect(Collectors.toList());

		redisTemplate.delete(keysToDelete);

		redisTemplate.opsForZSet().remove(USER_ZSET_KEY, ids.stream()
			.map(String::valueOf)
			.toArray());
	}

	private String serializeUserCache(UserCache userCache) {
		try {
			return objectMapper.writeValueAsString(userCache);
		} catch (JsonProcessingException e) {
			log.error("Redis 저장 실패: UserCache 직렬화 오류", e);
			return null;
		}
	}

	private UserCache deserializeUserCache(String jsonValue) {
		if (jsonValue == null) return null;
		try {
			return objectMapper.readValue(jsonValue, UserCache.class);
		} catch (Exception e) {
			log.error("Redis 조회 실패: UserCache 역직렬화 오류, 데이터={}", jsonValue, e);
			return null;
		}
	}
}

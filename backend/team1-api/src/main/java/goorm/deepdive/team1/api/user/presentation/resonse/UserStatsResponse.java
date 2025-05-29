package goorm.deepdive.team1.api.user.presentation.resonse;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;

@Builder
public record UserStatsResponse(
	Long total,
	Map<String, Long> genderStats,
	Map<String, Long> regionStats,
	Map<String, Long> ageStats
) {
	public static UserStatsResponse from(Map<String, Object> stats) {
		return UserStatsResponse.builder()
			.total((Long) stats.getOrDefault("total", 0L))
			.genderStats(new HashMap<>((Map<String, Long>) stats.getOrDefault("genderStats", new HashMap<>())))
			.regionStats(new HashMap<>((Map<String, Long>) stats.getOrDefault("regionStats", new HashMap<>())))
			.ageStats(new HashMap<>((Map<String, Long>) stats.getOrDefault("ageStats", new HashMap<>())))
			.build();
	}
}

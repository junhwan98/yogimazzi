package goorm.deepdive.team1.domain.user.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.domain.user.domain.enums.AgeGroups;

public interface UserRepository {
	User save(User user);

	Optional<User> findByIdAndDeletedAtIsNull(Long id);

	UserCache getUserCache(Long id);

	Page<UserCache> findAllCache(Pageable pageable);

	Page<User> findAll(Pageable pageable);

	void deleteById(Long id);

	Page<UserDocument> searchByRoadAddress(String keyword, Pageable pageable);
  
	Page<UserDocument> searchByRegionAddress(String keyword, Pageable pageable);

	Page<UserDocument> searchByName(String name, Pageable pageable);

	void saveCache(UserCache userCache);

	void saveAllCache(List<UserCache> userCaches);

	void saveDocument(UserDocument userDocument);

	Map<String, Object> searchUserStatistics(List<String> gender, List<String> region, List<AgeGroups> ageGroups);

	List<UserDocument> searchHeatMap(List<String> region, List<AgeGroups> ageGroups);

	List<Long> findIdsByDeletedAtIsNotNull();

	void deleteScheduling(List<Long> ids);

	}

package user.application;

import static goorm.deepdive.team1.domain.user.domain.enums.AgeGroups.TWENTIES;
import static goorm.deepdive.team1.domain.user.domain.enums.Gender.FEMALE;
import static goorm.deepdive.team1.domain.user.domain.enums.Gender.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.user.application.UserQueryService;
import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.domain.user.domain.enums.AgeGroups;
import mock.repository.FakeUserRepository;

public class UserQueryServiceTest {
	private UserQueryService userQueryService;
	private FakeUserRepository fakeUserRepository;

	@BeforeEach
	public void init() {
		fakeUserRepository = new FakeUserRepository();
		userQueryService = new UserQueryService(fakeUserRepository);

		User user1 = fakeUserRepository.save(User.create("test1", "test1@email.com", "01011111111",
			Address.create(1,1,"region1","road1","1"), MALE, 20));
		fakeUserRepository.saveCache(UserCache.from(user1));
		fakeUserRepository.saveDocument(UserDocument.from(user1));

		User user2 = fakeUserRepository.save(User.create("test2", "test2@email.com", "01022222222",
			Address.create(2,2,"region2","2","2"), FEMALE, 20));
		fakeUserRepository.saveDocument(UserDocument.from(user2));

		User user3 = fakeUserRepository.save(User.create("test3", "test3@email.com", "01033333333",
			Address.create(3,3,"region3","road3","3"), MALE, 20));
		fakeUserRepository.saveDocument(UserDocument.from(user3));

		User user4 = User.create("test3", "test3@email.com", "01033333333",
			Address.create(3,3,"region3","road3","3"), MALE, 20);
		user4.markAsDeleted();
		fakeUserRepository.save(user4);
	}

	@Test
	@DisplayName("getUserCacheById는 userCache를 조회한다.")
	public void getUserCacheByIdRedis_Success() {
		// given
		Long id = 1L;

		// when
		UserCache response = userQueryService.getUserCacheById(id);

		// then
		assertEquals(id, response.getId());
		assertEquals("test1", response.getName());
		assertEquals("test1@email.com", response.getEmail());
	}

	@Test
	@DisplayName("getById는 user를 조회한다.")
	public void getById_Success() {
		// given
		Long id = 2L;

		// when
		User response = userQueryService.getById(id);

		// then
		assertEquals(id, response.getId());
		assertEquals("test2", response.getName());
		assertEquals("test2@email.com", response.getEmail());
	}

	@Test
	@DisplayName("getCaches는 userCache 목록이 페이징 된 상태로 조회된다.")
	public void getCaches_Success() {
		// given
		Pageable pageable = PageRequest.of(0, 10);

		// when
		Page<UserCache> response = userQueryService.getCaches(pageable);

		// then
		assertEquals(1, response.getTotalElements());
		assertEquals(1, response.getTotalPages());
	}

	@Test
	@DisplayName("getCachesFromUsers는 user list를 조회하여 userCache로 변환해서 페이징 된 상태로 조회된다.")
	public void getCachesFromUsers_Success() {
		// given
		Pageable pageable = PageRequest.of(0, 10);

		// when
		Page<UserCache> response = userQueryService.getCachesFromUsers(pageable);

		// then
		assertEquals(3, response.getTotalElements());
		assertEquals(1, response.getTotalPages());
	}

	@Test
	@DisplayName("getUsersByRoadAddressKeyword는 도로명 주소를 기반으로 페이징 된 상태로 유저 목록을 조회한다.")
	public void getUsersByRoadAddressKeyword_Success() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		String keyword = "road";

		// when
		Page<UserDocument> response = userQueryService.getUsersByRoadAddressKeyword(keyword, pageable);

		// then
		assertEquals(2, response.getTotalElements());
	}

	@Test
	@DisplayName("getUsersByRegionAddressKeyword는 지번 주소를 기반으로 페이징 된 상태로 유저 목록을 조회한다.")
	public void getUsersByRegionAddressKeyword_Success() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		String keyword = "region";

		// when
		Page<UserDocument> response = userQueryService.getUsersByRegionAddressKeyword(keyword, pageable);

		// then
		assertEquals(3, response.getTotalElements());
	}

	@Test
	@DisplayName("getUsersByName은 유저 이름을 기반으로 페이징 된 상태로 유저 목록을 조회한다.")
	public void getUsersByName_Success() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		String keyword = "test";

		// when
		Page<UserDocument> response = userQueryService.getUsersByName(keyword, pageable);

		// then
		assertEquals(3, response.getTotalElements());
	}

	@Test
	@DisplayName("getUserStatics는 유저 통계 정보를 조회한다.")
	public void getUserStatics_Success() {
		// given
		List<String> gender = List.of(MALE.getValue());
		List<String> region = List.of("1");
		List<AgeGroups> ageGroups = List.of(TWENTIES);

		// when
		Map<String, Object> response = userQueryService.getUserStatistics(gender, region, ageGroups);

		// then
		assertEquals(4, response.size());
		assertEquals(1L, response.get("total"));
		Map<String, Long> expectedGenderStats = new LinkedHashMap<>();
		expectedGenderStats.put("남자", 1L);
		assertEquals(expectedGenderStats, response.get("genderStats"));

		Map<String, Long> expectedRegionStats = new LinkedHashMap<>();
		expectedRegionStats.put("1", 0L);
		assertEquals(expectedRegionStats, response.get("regionStats"));

		Map<String, Long> expectedAgeStats = new LinkedHashMap<>();
		expectedAgeStats.put("20.0-30.0", 1L);
		assertEquals(expectedAgeStats, response.get("ageStats"));
	}

	@Test
	@DisplayName("getUsersHeadMap은 해당 조건에 포함되어 있는 유저들의 document 목록을 조회한다")
	public void getUsersHeatMap_Success() {
		// given
		List<String> region = List.of("1");
		List<AgeGroups> ageGroups = List.of(TWENTIES);

		// when
		List<UserDocument> response = userQueryService.getUsersHeatMap(region, ageGroups);

		// then
		assertEquals(1, response.size());

		UserDocument userDocument = response.getFirst();
		assertEquals("test1", userDocument.getName());
		assertEquals("1", userDocument.getRegion());
	}

	@Test
	@DisplayName("findIdsByDeletedAtIsNotNull은 DeletedAt이 Null인 값만 조회한다.")
	public void findIdsByDeletedAtIsNotNull_Success() {
		// given
		// when
		List<Long> result = userQueryService.findIdsByDeletedAtIsNotNull();

		// then
		assertEquals(3, result.size());
	}
}

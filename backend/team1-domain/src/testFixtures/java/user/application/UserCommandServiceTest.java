package user.application;

import static goorm.deepdive.team1.domain.user.domain.enums.Gender.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.user.application.UserCommandService;
import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.domain.user.domain.enums.Gender;
import mock.repository.FakeUserRepository;

public class UserCommandServiceTest {
	private UserCommandService userCommandService;
	private FakeUserRepository fakeUserRepository;

	private User user;
	private User user1;
	private static final Long ID = 1L;
	private static final String NAME = "test";
	private static final String EMAIL = "test@email.com";
	private static final String PHONE_NUMBER = "01011111111";
	private static final Gender GENDER = MALE;
	private static final Integer AGE = 25;

	private Address address;
	private static final double X = 22.456789;
	private static final double Y = 33.456789;
	private static final String REGION_ADDRESS = "REGION";
	private static final String ROAD_ADDRESS = "ROAD";
	private static final String REGION = "SEOUL";

	@BeforeEach
	public void init() {
		fakeUserRepository = new FakeUserRepository();
		userCommandService = new UserCommandService(fakeUserRepository);

		Address address = Address.builder()
			.id(1L)
			.x(X)
			.y(Y)
			.regionAddress(REGION_ADDRESS)
			.roadAddress(ROAD_ADDRESS)
			.region(REGION)
			.build();

		user = fakeUserRepository.save(User.create("test1", "test1@email.com", "01011111111",
			address, MALE, 20));
		fakeUserRepository.saveDocument(UserDocument.from(user));

		user1 = fakeUserRepository.save(User.create("test2", "test2@email.com", "01022222222",
			address, MALE, 25));
		fakeUserRepository.saveDocument(UserDocument.from(user1));
	}


	@Test
	@DisplayName("create는 user 객체를 생성한다.")
	public void create_success(){
		// given
		// when
		User response = userCommandService.create(NAME, EMAIL, PHONE_NUMBER, address, GENDER, AGE);

		// then
		assertEquals(NAME, response.getName());
		assertEquals(EMAIL, response.getEmail());
		assertEquals(PHONE_NUMBER, response.getPhoneNumber());
		assertEquals(GENDER, response.getGender());
		assertEquals(AGE, response.getAge());
	}

	@Test
	@DisplayName("update는 user를 수정할 수 있다.")
	public void update_success(){
		// given
		String updateName = "update";
		String updateEmail = "update@email.com";
		String updatePhoneNumber = "01022222222";

		// when
		userCommandService.update(user, updateName, updateEmail, updatePhoneNumber, GENDER, AGE, address);

		// then
		assertEquals(updateName, user.getName());
		assertEquals(updateEmail, user.getEmail());
		assertEquals(updatePhoneNumber, user.getPhoneNumber());
	}

	@Test
	@DisplayName("delete는 user의 deletedAt을 현재 시간으로 수정한다.")
	public void delete_success(){
		// given
		// when
		userCommandService.delete(user);

		// then
		assertTrue(user.isDeleted());
	}

	@Test
	@DisplayName("saveCache는 userCache를 저장한다.")
	public void saveCache_success() {
		// given
		UserCache userCache = UserCache.from(user);

		// when
		userCommandService.saveCache(userCache);

		// then
		assertEquals(userCache, fakeUserRepository.getUserCache(user.getId()));
	}

	@Test
	@DisplayName("cleanUpDeletedUsers는 삭제된 유저들을 정리한다.")
	public void cleanUpDeletedUsers_success() {
		// given
		User user2 = fakeUserRepository.save(User.create("test2", "test2@email.com", "01022222222",
			address, MALE, 25));

		user.markAsDeleted();
		user2.markAsDeleted();
		fakeUserRepository.save(user);
		fakeUserRepository.save(user2);

		List<Long> deletedIds = List.of(user.getId(), user2.getId());

		// when
		userCommandService.cleanUpDeletedUsers(deletedIds);

		// then
		assertTrue(fakeUserRepository.findByIdAndDeletedAtIsNull(user.getId()).isEmpty());
		assertTrue(fakeUserRepository.findByIdAndDeletedAtIsNull(user2.getId()).isEmpty());
	}

	@Test
	@DisplayName("saveAllCaches는 userCaches 리스트를 저장한다.")
	public void saveAllCaches_success() {
		// given
		UserCache userCache = UserCache.from(user);
		UserCache userCache1 = UserCache.from(user1);
		List<UserCache> userCaches = List.of(userCache, userCache1);

		// when
		userCommandService.saveAllCaches(userCaches);

		// then
		assertEquals(userCache, fakeUserRepository.getUserCache(user.getId()));
		assertEquals(userCache1, fakeUserRepository.getUserCache(user1.getId()));
	}
}

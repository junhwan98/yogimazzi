package user.domain;

import static goorm.deepdive.team1.domain.user.domain.enums.Gender.FEMALE;
import static goorm.deepdive.team1.domain.user.domain.enums.Gender.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.enums.Gender;

public class UserDomainTest {
	private User user;
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
		address = Address.builder()
			.id(ID)
			.x(X)
			.y(Y)
			.regionAddress(REGION_ADDRESS)
			.roadAddress(ROAD_ADDRESS)
			.region(REGION)
			.build();

		user = User.create(NAME, EMAIL, PHONE_NUMBER, address, GENDER, AGE);
	}

	@Test
	@DisplayName("create 메소드는 user 객체를 생성할 수 있다.")
	public void create_Success() {
		// when
		// then
		assertNotNull(user);
		assertEquals(NAME, user.getName());
		assertEquals(EMAIL, user.getEmail());
		assertEquals(PHONE_NUMBER, user.getPhoneNumber());
		assertEquals(GENDER, user.getGender());
		assertEquals(AGE, user.getAge());
	}

	@Test
	@DisplayName("updateName 메소드는 user의 name을 수정할 수 있다.")
	public void updateName_Success() {
		// given
		String updateName = "update";

		// when
		user.updateName(updateName);

		// then
		assertEquals(updateName, user.getName());
	}

	@Test
	@DisplayName("updateEmail 메소드는 user의 email을 수정할 수 있다.")
	public void updateEmail_Success() {
		// given
		String updateEmail = "update@email.com";

		// when
		user.updateEmail(updateEmail);

		// then
		assertEquals(updateEmail, user.getEmail());
	}

	@Test
	@DisplayName("updatePhoneNumber 메소드는 user의 phoneNumber를 수정할 수 있다.")
	public void updatePhoneNumber_Success() {
		// given
		String updatePhoneNumber = "01022222222";

		// when
		user.updatePhoneNumber(updatePhoneNumber);

		// then
		assertEquals(updatePhoneNumber, user.getPhoneNumber());
	}

	@Test
	@DisplayName("updateAddress 메소드는 user의 address를 수정할 수 있다.")
	public void updateAddress_Success() {
		// given
		address = Address.builder()
			.id(ID)
			.x(Y)
			.y(X)
			.regionAddress(ROAD_ADDRESS)
			.roadAddress(REGION_ADDRESS)
			.region(REGION)
			.build();

		// when
		user.updateAddress(address);

		// then
		Address userAddress = user.getAddress();
		assertEquals(X, userAddress.getY());
		assertEquals(Y, userAddress.getX());
		assertEquals(REGION_ADDRESS, userAddress.getRoadAddress());
		assertEquals(ROAD_ADDRESS, userAddress.getRegionAddress());
	}

	@Test
	@DisplayName("updateGender 메소드는 user의 gender를 수정할 수 있다.")
	public void updateGender_Success() {
		// given
		Gender updateGender = FEMALE;

		// when
		user.updateGender(updateGender);

		// then
		assertEquals(updateGender, user.getGender());
	}

	@Test
	@DisplayName("updateAge 메소드는 user의 age를 수정할 수 있다.")
	public void updateAge_Success() {
		// given
		Integer updateAge = 35;

		// when
		user.updateAge(updateAge);

		// then
		assertEquals(updateAge, user.getAge());
	}
}

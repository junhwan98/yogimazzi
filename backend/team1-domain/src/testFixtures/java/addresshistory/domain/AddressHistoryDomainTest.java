package addresshistory.domain;

import static goorm.deepdive.team1.domain.user.domain.enums.Gender.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.enums.Gender;

public class AddressHistoryDomainTest {
	private static final Long ID = 1L;

	private User user;
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

		user = User.builder()
			.id(ID)
			.name(NAME)
			.email(EMAIL)
			.phoneNumber(PHONE_NUMBER)
			.address(address)
			.gender(GENDER)
			.age(AGE)
			.build();
	}

	@Test
	@DisplayName("create 메소드는 AddressHistory 객체를 생성할 수 있다.")
	public void create_Success() {
		// when
		AddressHistory addressHistory = AddressHistory.create(user, address);

		// then
		assertNotNull(addressHistory);
		assertEquals(address, addressHistory.getAddress());
		assertEquals(user, addressHistory.getUser());
	}
}

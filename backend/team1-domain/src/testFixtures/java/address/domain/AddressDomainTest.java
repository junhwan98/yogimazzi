package address.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import goorm.deepdive.team1.domain.address.domain.Address;

public class AddressDomainTest {
	private Address address;
	private static final Long ID = 1L;
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
	}

	@Test
	@DisplayName("create 메소드는 Address 객체를 생성할 수 있다.")
	public void create_Success() {
		// when
		// then
		assertNotNull(address);
		assertEquals(X, address.getX());
		assertEquals(Y, address.getY());
		assertEquals(REGION_ADDRESS, address.getRegionAddress());
		assertEquals(ROAD_ADDRESS, address.getRoadAddress());
		assertEquals(REGION, address.getRegion());
	}

	@Test
	@DisplayName("updateX 메소드는 address의 x 좌표를 수정할 수 있다.")
	public void updateX_Success() {
		// given
		double updateX = 50.123456;

		// when
		address.updateX(updateX);

		// then
		assertEquals(updateX, address.getX());
	}

	@Test
	@DisplayName("updateY 메소드는 address의 y 좌표를 수정할 수 있다.")
	public void updateY_Success() {
		// given
		double updateY = 60.654321;

		// when
		address.updateY(updateY);

		// then
		assertEquals(updateY, address.getY());
	}

	@Test
	@DisplayName("updateRegionAddress 메소드는 address의 regionAddress를 수정할 수 있다.")
	public void updateRegionAddress_Success() {
		// given
		String updateRegionAddress = "NEW_REGION";

		// when
		address.updateRegionAddress(updateRegionAddress);

		// then
		assertEquals(updateRegionAddress, address.getRegionAddress());
	}

	@Test
	@DisplayName("updateRoadAddress 메소드는 address의 roadAddress를 수정할 수 있다.")
	public void updateRoadAddress_Success() {
		// given
		String updateRoadAddress = "NEW_ROAD";

		// when
		address.updateRoadAddress(updateRoadAddress);

		// then
		assertEquals(updateRoadAddress, address.getRoadAddress());
	}

	@Test
	@DisplayName("updateRegion 메소드는 address의 region을 수정할 수 있다.")
	public void updateRegion_Success() {
		// given
		String updateRegion = "BUSAN";

		// when
		address.updateRegion(updateRegion);

		// then
		assertEquals(updateRegion, address.getRegion());
	}
}

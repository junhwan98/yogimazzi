package address.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import goorm.deepdive.team1.domain.address.application.AddressCommandService;
import goorm.deepdive.team1.domain.address.domain.Address;
import mock.repository.FakeAddressRepository;

public class AddressCommandServiceTest {
	private AddressCommandService addressCommandService;

	@BeforeEach
	public void init() {
		FakeAddressRepository fakeAddressRepository = new FakeAddressRepository();
		addressCommandService = new AddressCommandService(fakeAddressRepository);
	}

	@Test
	@DisplayName("create는 Address를 생성한다.")
	public void create_Success() {
		// given
		Address address = Address.builder()
			.id(1L)
			.x(1.0)
			.y(2.0)
			.roadAddress("roadAddress")
			.regionAddress("regionAddress")
			.region("region")
			.build();

		// when
		Address response = addressCommandService.save(address);

		// then
		assertEquals(address.getId(), response.getId());
		assertEquals(address.getX(), response.getX());
		assertEquals(address.getY(), response.getY());
		assertEquals(address.getRoadAddress(), response.getRoadAddress());
		assertEquals(address.getRegionAddress(), response.getRegionAddress());
		assertEquals(address.getRegion(), response.getRegion());
	}
}

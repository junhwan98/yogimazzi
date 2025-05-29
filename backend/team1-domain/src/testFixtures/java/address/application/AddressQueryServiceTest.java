package address.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import goorm.deepdive.team1.domain.address.application.AddressQueryService;
import goorm.deepdive.team1.domain.address.domain.Address;
import mock.repository.FakeAddressRepository;

public class AddressQueryServiceTest {
	private AddressQueryService addressQueryService;

	@BeforeEach
	public void init() {
		FakeAddressRepository fakeAddressRepository = new FakeAddressRepository();
		addressQueryService = new AddressQueryService(fakeAddressRepository);

		fakeAddressRepository.save(
			Address.builder()
			.x(1.0)
			.y(2.0)
			.roadAddress("roadAddress1")
			.regionAddress("regionAddress1")
			.region("region1")
			.build()
		);

		fakeAddressRepository.save(
			Address.builder()
				.x(2.0)
				.y(3.0)
				.roadAddress("roadAddress2")
				.regionAddress("regionAddress2")
				.region("region2")
				.build()
		);
	}

	@Test
	@DisplayName("findByRoadAddress는 roadAddress를 기반으로 주소 목록을 조회한다.")
	public void findByRoadAddress_Success() {
		// given
		String roadAddress = "Address";

		// when
		Address response = addressQueryService.findByRoadAddress(roadAddress);

		// then
		assertEquals(1L, response.getId());
		assertEquals("roadAddress1", response.getRoadAddress());
	}
}

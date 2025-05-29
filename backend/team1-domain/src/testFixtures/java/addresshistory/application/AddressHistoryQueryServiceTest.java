package addresshistory.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.addresshistory.application.AddressHistoryQueryService;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;
import goorm.deepdive.team1.domain.user.domain.User;
import mock.r.TestEntityUtils;
import mock.repository.FakeAddressHistoryRepository;

public class AddressHistoryQueryServiceTest {
	private AddressHistoryQueryService addressHistoryQueryService;

	private Address address2;
	private AddressHistory addressHistory1;
	private AddressHistory addressHistory2;


	@BeforeEach
	public void init() {
		FakeAddressHistoryRepository fakeAddressHistoryRepository = new FakeAddressHistoryRepository();
		addressHistoryQueryService = new AddressHistoryQueryService(fakeAddressHistoryRepository);

		User user = User.builder().id(1L)
			.address(address2)
			.build();

		Address address1 = Address.builder()
			.id(1L)
			.x(1.0)
			.y(1.0)
			.roadAddress("roadAddress1")
			.regionAddress("regionAddress1")
			.region("region1")
			.build();

		address2 = Address.builder()
			.id(2L)
			.x(2.0)
			.y(2.0)
			.roadAddress("roadAddress2")
			.regionAddress("regionAddress2")
			.region("region2")
			.build();


		addressHistory1 = AddressHistory.create(user, address1);
		TestEntityUtils.setCreatedAt(addressHistory1, LocalDateTime.now());

		addressHistory2 = AddressHistory.create(user, address2);
		TestEntityUtils.setCreatedAt(addressHistory2, LocalDateTime.now());

		fakeAddressHistoryRepository.save(addressHistory1);
		fakeAddressHistoryRepository.save(addressHistory2);
		fakeAddressHistoryRepository.saveAllCaches(List.of(addressHistory1, addressHistory2));
	}

	@Test
	@DisplayName("getByUserIdDeletedAtIsNull은 삭제되지 않은 AddressHistory를 조회한다.")
	public void getByUserIdDeletedAtIsNull_Success() {
		// given
		Long userId = 1L;

		// when
		List<AddressHistory> result = addressHistoryQueryService.getByUserIdDeletedAtIsNull(userId);

		// then
		assertEquals(2, result.size());
		assertEquals(addressHistory1.getAddress().getRoadAddress(), result.get(0).getAddress().getRoadAddress());
		assertEquals(addressHistory2.getAddress().getRoadAddress(), result.get(1).getAddress().getRoadAddress());
	}

	@Test
	@DisplayName("getByUserIdDeletedAtIsNull은 해당하는 데이터가 없으면 빈 리스트를 반환한다.")
	public void getByUserIdDeletedAtIsNull_Empty() {
		// given
		Long invalidUserId = 100L;

		// when
		List<AddressHistory> result = addressHistoryQueryService.getByUserIdDeletedAtIsNull(invalidUserId);

		// then
		assertTrue(result.isEmpty());
	}

	@Test
	@DisplayName("getAddressHistoryCaches는 특정 유저의 AddressHistoryCache를 조회한다.")
	public void getAddressHistoryCaches_Success() {
		// given
		Long userId = 1L;

		// when
		List<AddressHistoryCache> result = addressHistoryQueryService.getAddressHistoryCaches(userId);

		// then
		assertEquals(2, result.size());
		assertEquals("regionAddress1", result.getFirst().getRegionAddress());
	}

	@Test
	@DisplayName("getAddressHistoryCaches는 데이터가 없으면 빈 리스트를 반환한다.")
	public void getAddressHistoryCaches_Empty() {
		// given
		Long invalidUserId = 100L;

		// when
		List<AddressHistoryCache> result = addressHistoryQueryService.getAddressHistoryCaches(invalidUserId);

		// then
		assertTrue(result.isEmpty());
	}
}

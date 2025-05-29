package addresshistory.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.addresshistory.application.AddressHistoryCommandService;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.user.domain.User;
import mock.r.TestEntityUtils;
import mock.repository.FakeAddressHistoryRepository;

public class AddressHistoryCommandServiceTest {
	private AddressHistoryCommandService addressHistoryCommandService;
	private FakeAddressHistoryRepository fakeAddressHistoryRepository;

	private User user;
	private Address address;
	private AddressHistory addressHistory;

	@BeforeEach
	public void init() {
		fakeAddressHistoryRepository = new FakeAddressHistoryRepository();
		addressHistoryCommandService = new AddressHistoryCommandService(fakeAddressHistoryRepository);

		address = Address.builder()
			.id(1L)
			.x(1.0)
			.y(1.0)
			.regionAddress("regionAddress")
			.roadAddress("roadAddress")
			.region("region")
			.build();

		user = User.builder()
			.id(1L)
			.address(address)
			.build();

		addressHistory = AddressHistory.builder()
			.id(1L)
			.user(user)
			.address(address)
			.build();

		TestEntityUtils.setCreatedAt(addressHistory, LocalDateTime.now());

		fakeAddressHistoryRepository.saveAllCaches(List.of(addressHistory));
	}

	@Test
	@DisplayName("create는 AddressHistory를 생성한다.")
	public void create_Success() {
		// given
		// when
		AddressHistory response = addressHistoryCommandService.create(user, address);

		// then
		assertEquals(user.getId(), response.getUser().getId());
	}

	@Test
	@DisplayName("saveAll은 AddressHistory 리스트를 캐시로 변환하여 모두 저장한다.")
	public void saveAll_Success() {
		// given
		List<AddressHistory> request = List.of(addressHistory);

		// when
		addressHistoryCommandService.saveAll(request);

		// then
		assertEquals(request.size(), fakeAddressHistoryRepository.findCacheByUserIdAndDeletedAtIsNull(1L).size());
	}

	@Test
	@DisplayName("cleanUpDeletedAddressHistories는 해당 매개변수의 있는 유저와 관련된 정보를 삭제한다.")
	public void cleanUpDeletedAddressHistories_Success() {
		// given
		List<Long> request = List.of(user.getId());

		// when
		addressHistoryCommandService.cleanUpDeletedAddressHistories(request);

		// then
		assertEquals(0, fakeAddressHistoryRepository.findCacheByUserIdAndDeletedAtIsNull(1L).size());
		assertEquals(0, fakeAddressHistoryRepository.findByUserIdAndDeletedAtIsNull(1L).size());
	}
}

package goorm.deepdive.team1.domain.addresshistory.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.infrastructure.AddressHistoryProducer;
import goorm.deepdive.team1.domain.addresshistory.infrastructure.AddressHistoryRepository;
import goorm.deepdive.team1.domain.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressHistoryCommandService {
	private final AddressHistoryRepository addressHistoryRepository;
	private final AddressHistoryProducer addressHistoryProducer;

	public void create(User user) {
		AddressHistory addressHistory = AddressHistory.create(user);
		addressHistoryProducer.sendMessageToCreate(addressHistoryRepository.save(addressHistory));
	}

	public void update(User user) {
		AddressHistory addressHistory = AddressHistory.create(user);
		addressHistoryProducer.sendMessageToDelete(addressHistoryRepository.save(addressHistory));
	}

	public void saveAll(List<AddressHistory> addressHistories) {
		addressHistoryRepository.saveAllCaches(addressHistories);
	}

	public void cleanUpDeletedAddressHistories(List<Long> userIds) {
		addressHistoryRepository.deleteScheduling(userIds);
	}
}

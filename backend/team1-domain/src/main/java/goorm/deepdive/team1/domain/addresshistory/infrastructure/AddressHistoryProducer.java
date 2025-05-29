package goorm.deepdive.team1.domain.addresshistory.infrastructure;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;

public interface AddressHistoryProducer {
	void sendMessageToCreate(AddressHistory addressHistory);

	void sendMessageToDelete(AddressHistory addressHistory);
}

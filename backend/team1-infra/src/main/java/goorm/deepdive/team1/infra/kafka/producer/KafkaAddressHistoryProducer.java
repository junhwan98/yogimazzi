package goorm.deepdive.team1.infra.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.infrastructure.AddressHistoryProducer;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaAddressHistoryProducer implements AddressHistoryProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ObjectMapper objectMapper;

	private static final String CREATE_ADDRESS_HISTORY = "create-address-history";
	private static final String DELETE_ADDRESS_HISTORY = "delete-address-history";

	@Override
	public void sendMessageToCreate(AddressHistory addressHistory) {
		try {
			String addressHistoryJson = objectMapper.writeValueAsString(addressHistory);
			kafkaTemplate.send(CREATE_ADDRESS_HISTORY, addressHistoryJson);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendMessageToDelete(AddressHistory addressHistory) {
		try {
			String addressHistoryJson = objectMapper.writeValueAsString(addressHistory);
			kafkaTemplate.send(DELETE_ADDRESS_HISTORY, addressHistoryJson);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}

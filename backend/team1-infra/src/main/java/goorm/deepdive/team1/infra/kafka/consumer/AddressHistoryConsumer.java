package goorm.deepdive.team1.infra.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;
import goorm.deepdive.team1.infra.kafka.exception.KafkaProcessingException;
import goorm.deepdive.team1.infra.repository.redis.RedisAddressHistoryRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressHistoryConsumer {
	private final RedisAddressHistoryRepository redisAddressHistoryRepository;
	private final ObjectMapper objectMapper;

	private static final String CREATE_ADDRESS_HISTORY = "create-address-history";
	private static final String DELETE_ADDRESS_HISTORY = "delete-address-history";

	@KafkaListener(
		topics = CREATE_ADDRESS_HISTORY,
		groupId = "consumer-group",
		containerFactory = "consumerGroup"
	)
	public void consumeCreate(ConsumerRecord<String, Object> record) {
		try {
			String json = record.value().toString();
			AddressHistory addressHistory = objectMapper.readValue(json, AddressHistory.class);

			AddressHistoryCache addressHistoryCache = AddressHistoryCache.from(addressHistory);

			redisAddressHistoryRepository.save(addressHistoryCache);

			System.out.println("✅ Consumed Address-History: " + addressHistory.getAddress());
		} catch (Exception e) {
			throw new KafkaProcessingException();
		}
	}


	@KafkaListener(
		topics = DELETE_ADDRESS_HISTORY,
		groupId = "consumer-group",
		containerFactory = "consumerGroup"
	)
	public void consumeDelete(ConsumerRecord<String, Object> record) {
		try {
			String json = record.value().toString();
			AddressHistory addressHistory = objectMapper.readValue(json, AddressHistory.class);

			redisAddressHistoryRepository.deleteByUserId(addressHistory.getUser().getId());

			System.out.println("✅ Consumed Address-History-delete: " + addressHistory.getUser().getId());
		} catch (Exception e) {
			throw new KafkaProcessingException();
		}
	}
}

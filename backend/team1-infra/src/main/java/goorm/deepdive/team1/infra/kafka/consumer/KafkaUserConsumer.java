package goorm.deepdive.team1.infra.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.infra.kafka.exception.KafkaProcessingException;
import goorm.deepdive.team1.infra.repository.elastic.ElasticUserRepository;
import goorm.deepdive.team1.infra.repository.redis.RedisUserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaUserConsumer {
	private final RedisUserRepository redisUserRepository;
	private final ElasticUserRepository elasticUserRepository;
	private final ObjectMapper objectMapper;

	@KafkaListener(
		topics = "create-user",
		groupId = "consumer-group",
		containerFactory = "consumerGroup"
	)
	public void consumeToCreate(ConsumerRecord<String, Object> record) {
		try {
			String json = record.value().toString();
			User user = objectMapper.readValue(json, User.class);

			UserCache userCache = UserCache.from(user);
			UserDocument userDocument = UserDocument.from(user);

			redisUserRepository.save(userCache);
			elasticUserRepository.save(userDocument);
		} catch (Exception e) {
			throw new KafkaProcessingException();
		}
	}

	@KafkaListener(
		topics = "update-user",
		groupId = "consumer-group",
		containerFactory = "consumerGroup"
	)
	public void consumeToUpdate(ConsumerRecord<String, Object> record) {
		try {
			String json = record.value().toString();
			User user = objectMapper.readValue(json, User.class);

			UserCache userCache = UserCache.from(user);
			UserDocument userDocument = UserDocument.from(user);

			redisUserRepository.save(userCache);
			elasticUserRepository.save(userDocument);
		} catch (Exception e) {
			throw new KafkaProcessingException();
		}
	}

	@KafkaListener(
		topics = "delete-user",
		groupId = "consumer-group",
		containerFactory = "consumerGroup"
	)
	public void consumeToDelete(ConsumerRecord<String, Object> record) throws JsonProcessingException {
		try {
			String json = record.value().toString();
			User user = objectMapper.readValue(json, User.class);

			UserCache userCache = UserCache.from(user);
			UserDocument userDocument = UserDocument.from(user);

			redisUserRepository.save(userCache);
			elasticUserRepository.save(userDocument);
		} catch (Exception e) {
			throw new KafkaProcessingException();
		}
	}
}

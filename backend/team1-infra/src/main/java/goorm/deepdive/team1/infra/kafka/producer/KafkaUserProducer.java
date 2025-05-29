package goorm.deepdive.team1.infra.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import goorm.deepdive.team1.domain.user.infrastructure.UserProducer;
import goorm.deepdive.team1.domain.user.domain.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaUserProducer implements UserProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ObjectMapper objectMapper;

	private static final String CREATE_USER = "create-user";
	private static final String UPDATE_USER = "update-user";

	@Override
	public void sendMessageToCreate(User user) {
		try {
			String userJson = objectMapper.writeValueAsString(user);
			kafkaTemplate.send(CREATE_USER, userJson);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendMessageToUpdate(User user) {
		try {
			String userJson = objectMapper.writeValueAsString(user);
			kafkaTemplate.send(UPDATE_USER, userJson);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}

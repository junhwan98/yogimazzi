package goorm.deepdive.team1.infra.kafka.exception;

import static goorm.deepdive.team1.common.exception.GlobalExceptionCode.SERVER_ERROR;

import goorm.deepdive.team1.common.exception.CustomException;

public class KafkaProcessingException extends CustomException {
	public KafkaProcessingException() {
		super(SERVER_ERROR);
	}
}

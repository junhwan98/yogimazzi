package goorm.deepdive.team1.infra.repository.elastic.exception;

import static goorm.deepdive.team1.common.exception.GlobalExceptionCode.SERVER_ERROR;

import goorm.deepdive.team1.common.exception.CustomException;

public class ElasticQueryExecutionException extends CustomException {
	public ElasticQueryExecutionException() {
		super(SERVER_ERROR);
	}
}

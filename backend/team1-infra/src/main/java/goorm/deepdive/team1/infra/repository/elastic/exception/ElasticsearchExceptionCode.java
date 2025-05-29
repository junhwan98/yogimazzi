package goorm.deepdive.team1.infra.repository.elastic.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.HttpStatus;

import goorm.deepdive.team1.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ElasticsearchExceptionCode implements ExceptionCode {
	ELASTICSEARCH_EXCEPTION(INTERNAL_SERVER_ERROR, "쿼리 중 오류가 발생했습니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getCode() {
		return this.name();
	}
}

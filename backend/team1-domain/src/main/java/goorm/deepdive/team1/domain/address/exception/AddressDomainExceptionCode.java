package goorm.deepdive.team1.domain.address.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;

import goorm.deepdive.team1.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddressDomainExceptionCode implements ExceptionCode {
	ADDRESS_NOT_FOUND(NOT_FOUND, "해당 주소를 찾을 수 없습니다."),
	;

	private final HttpStatus status;
	private final String message;

	@Override
	public String getCode() {
		return this.name();
	}
}

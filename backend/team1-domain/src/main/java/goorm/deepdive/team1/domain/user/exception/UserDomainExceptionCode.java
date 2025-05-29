package goorm.deepdive.team1.domain.user.exception;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;

import goorm.deepdive.team1.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserDomainExceptionCode implements ExceptionCode {
	USER_NOT_FOUND(NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
	EMAIL_ALREADY_EXISTS(CONFLICT, "이미 존재하는 이메일입니다.")
	;

	private final HttpStatus status;
	private final String message;

	@Override
	public String getCode() {
		return this.name();
	}
}

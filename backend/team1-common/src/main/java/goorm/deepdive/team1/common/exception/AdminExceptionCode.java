package goorm.deepdive.team1.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum AdminExceptionCode implements ExceptionCode {
	NOT_ADMIN(FORBIDDEN, "관리자 전용 API입니다."),
	ADMIN_NOT_FOUND(NOT_FOUND, "해당 이메일의 관리자를 찾을 수 없습니다."),
	ALREADY_REGISTERED(CONFLICT, "이미 등록된 이메일입니다.");
	;

	private final HttpStatus status;
	private final String message;


	@Override
	public String getCode() {
		return this.name();
	}
}

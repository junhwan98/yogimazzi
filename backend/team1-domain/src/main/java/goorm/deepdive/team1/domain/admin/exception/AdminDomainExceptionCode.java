package goorm.deepdive.team1.domain.admin.exception;

import goorm.deepdive.team1.common.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum AdminDomainExceptionCode implements ExceptionCode {
	PASSWORD_MISMATCH(UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	ADMIN_NOT_FOUND(NOT_FOUND, "해당 이메일의 관리자를 찾을 수 없습니다."),
	EMAIL_ALREADY_EXISTS(CONFLICT, "이미 존재하는 이메일입니다."),
	CANNOT_CHANGE_OWN_ROLE(FORBIDDEN, "자신의 역할(Role)은 변경할 수 없습니다.");
	;

	private final HttpStatus status;
	private final String message;


	@Override
	public String getCode() {
		return this.name();
	}
}

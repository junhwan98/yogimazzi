package goorm.deepdive.team1.domain.user.exception;

import goorm.deepdive.team1.common.exception.CustomException;

import static goorm.deepdive.team1.domain.user.exception.UserDomainExceptionCode.EMAIL_ALREADY_EXISTS;

public class UserEmailAlreadyExistsException extends CustomException {
	public UserEmailAlreadyExistsException() {
		super(EMAIL_ALREADY_EXISTS);
	}
}

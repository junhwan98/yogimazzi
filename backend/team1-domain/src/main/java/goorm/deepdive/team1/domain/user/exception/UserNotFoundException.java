package goorm.deepdive.team1.domain.user.exception;

import static goorm.deepdive.team1.domain.user.exception.UserDomainExceptionCode.USER_NOT_FOUND;

import goorm.deepdive.team1.common.exception.CustomException;

public class UserNotFoundException extends CustomException {
	public UserNotFoundException() {
		super(USER_NOT_FOUND);
	}
}

package goorm.deepdive.team1.domain.admin.exception;

import goorm.deepdive.team1.common.exception.CustomException;

import static goorm.deepdive.team1.domain.admin.exception.AdminDomainExceptionCode.EMAIL_ALREADY_EXISTS;

public class AdminEmailAlreadyExistsException extends CustomException {
    public AdminEmailAlreadyExistsException() {
        super(EMAIL_ALREADY_EXISTS);
    }
}

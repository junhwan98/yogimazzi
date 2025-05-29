package goorm.deepdive.team1.domain.admin.exception;

import goorm.deepdive.team1.common.exception.CustomException;

import static goorm.deepdive.team1.domain.admin.exception.AdminDomainExceptionCode.PASSWORD_MISMATCH;

public class AdminPasswordMismatchException extends CustomException {
    public AdminPasswordMismatchException() {
        super(PASSWORD_MISMATCH);
    }
}

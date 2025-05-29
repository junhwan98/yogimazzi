package goorm.deepdive.team1.domain.admin.exception;

import goorm.deepdive.team1.common.exception.CustomException;

import static goorm.deepdive.team1.domain.admin.exception.AdminDomainExceptionCode.ADMIN_NOT_FOUND;

public class AdminNotFoundException extends CustomException {
    public AdminNotFoundException() {
        super(ADMIN_NOT_FOUND);
    }
}

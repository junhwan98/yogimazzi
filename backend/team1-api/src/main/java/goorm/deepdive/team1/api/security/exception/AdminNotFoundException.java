package goorm.deepdive.team1.api.security.exception;


import goorm.deepdive.team1.common.exception.CustomException;

import static goorm.deepdive.team1.api.security.exception.AdminDomainExceptionCode.ADMIN_NOT_FOUND;

public class AdminNotFoundException extends CustomException {
    public AdminNotFoundException() {
        super(ADMIN_NOT_FOUND);
    }
}
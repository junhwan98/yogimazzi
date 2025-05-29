package goorm.deepdive.team1.domain.admin.exception;

import goorm.deepdive.team1.common.exception.CustomException;

import static goorm.deepdive.team1.domain.admin.exception.AdminDomainExceptionCode.CANNOT_CHANGE_OWN_ROLE;

public class CannotChangeOwnRoleException extends CustomException {
    public CannotChangeOwnRoleException() {
        super(CANNOT_CHANGE_OWN_ROLE);
    }
}
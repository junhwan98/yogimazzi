package goorm.deepdive.team1.api.jwt.exception;

import goorm.deepdive.team1.common.exception.CustomException;

public class JwtSignatureInvalidException extends CustomException {
    public JwtSignatureInvalidException() {
        super(JwtExceptionCode.INVALID_SIGNATURE);
    }
}
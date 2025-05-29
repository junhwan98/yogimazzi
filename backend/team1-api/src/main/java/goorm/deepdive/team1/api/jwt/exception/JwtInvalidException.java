package goorm.deepdive.team1.api.jwt.exception;

import goorm.deepdive.team1.common.exception.CustomException;

public class JwtInvalidException extends CustomException {
    public JwtInvalidException() {
        super(JwtExceptionCode.INVALID_SIGNATURE);
    }
}
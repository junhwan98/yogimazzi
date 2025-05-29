package goorm.deepdive.team1.api.jwt.exception;

import goorm.deepdive.team1.common.exception.CustomException;

public class JwtExpiredException extends CustomException {
    public JwtExpiredException() {
        super(JwtExceptionCode.EXPIRED_TOKEN);
    }
}
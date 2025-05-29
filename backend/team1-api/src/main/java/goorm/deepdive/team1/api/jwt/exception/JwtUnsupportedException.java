package goorm.deepdive.team1.api.jwt.exception;

import goorm.deepdive.team1.common.exception.CustomException;

public class JwtUnsupportedException extends CustomException {
    public JwtUnsupportedException() {
        super(JwtExceptionCode.UNSUPPORTED_TOKEN);
    }
}
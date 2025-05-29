package goorm.deepdive.team1.api.jwt.exception;

import goorm.deepdive.team1.common.exception.CustomException;

public class JwtMalformedException extends CustomException {
    public JwtMalformedException() {
        super(JwtExceptionCode.MALFORMED_TOKEN);
    }
}
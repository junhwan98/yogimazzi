package goorm.deepdive.team1.api.jwt.exception;

import goorm.deepdive.team1.common.exception.CustomException;

public class JwtEmptyException  extends CustomException {
    public JwtEmptyException() {
        super(JwtExceptionCode.EMPTY_TOKEN);
    }
}
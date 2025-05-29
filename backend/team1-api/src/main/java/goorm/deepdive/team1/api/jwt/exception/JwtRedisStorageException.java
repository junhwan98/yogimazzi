package goorm.deepdive.team1.api.jwt.exception;

import goorm.deepdive.team1.common.exception.CustomException;

public class JwtRedisStorageException  extends CustomException {
    public JwtRedisStorageException() {
        super(JwtExceptionCode.REFRESH_EMPTY_TOKEN);
    }
}

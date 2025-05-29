package goorm.deepdive.team1.domain.address.exception.KakaoApiException;

import goorm.deepdive.team1.common.exception.CustomException;

public class RateLimitExceededException extends CustomException {
    public RateLimitExceededException() {
        super(KakaoApiExceptionCode.RATE_LIMIT_EXCEEDED);
    }
}

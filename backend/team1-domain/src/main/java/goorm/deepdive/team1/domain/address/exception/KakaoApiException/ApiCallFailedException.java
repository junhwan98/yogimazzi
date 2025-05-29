package goorm.deepdive.team1.domain.address.exception.KakaoApiException;

import goorm.deepdive.team1.common.exception.CustomException;

public class ApiCallFailedException extends CustomException {
    public ApiCallFailedException() {
        super(KakaoApiExceptionCode.API_CALL_FAILED);
    }
}

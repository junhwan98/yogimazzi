package goorm.deepdive.team1.domain.address.exception.KakaoApiException;

import goorm.deepdive.team1.common.exception.CustomException;

public class ServiceDisabledException extends CustomException {
    public ServiceDisabledException() {
        super(KakaoApiExceptionCode.SERVICE_DISABLED);
    }
}

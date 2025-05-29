package goorm.deepdive.team1.domain.address.exception.KakaoApiException;

import goorm.deepdive.team1.common.exception.CustomException;

public class InvalidRequestException extends CustomException {
    public InvalidRequestException() {super(KakaoApiExceptionCode.INVALID_REQUEST);}
}

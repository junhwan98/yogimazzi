package goorm.deepdive.team1.domain.address.exception.KakaoApiException;

import goorm.deepdive.team1.common.exception.CustomException;

import static goorm.deepdive.team1.domain.address.exception.KakaoApiException.KakaoApiExceptionCode.ADDRESS_NOT_FOUND;

public class AddressNotFoundException extends CustomException {
    public AddressNotFoundException() {
        super(ADDRESS_NOT_FOUND);
    }
}

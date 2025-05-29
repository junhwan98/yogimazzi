package goorm.deepdive.team1.domain.address.exception.KakaoApiException;

import static org.springframework.http.HttpStatus.*;

import goorm.deepdive.team1.common.exception.ExceptionCode;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KakaoApiExceptionCode implements ExceptionCode {
    INVALID_REQUEST(BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED(FORBIDDEN, "Kakao API 접근 권한이 없습니다."),
    RATE_LIMIT_EXCEEDED(TOO_MANY_REQUESTS, "Kakao API 요청 제한을 초과했습니다."),
    SERVICE_DISABLED(FORBIDDEN, "Kakao API 서비스가 비활성화되었습니다."),
    API_CALL_FAILED(INTERNAL_SERVER_ERROR,  "Kakao API 호출 중 문제가 발생했습니다."),
    ADDRESS_NOT_FOUND(NOT_FOUND, "요청한 주소를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
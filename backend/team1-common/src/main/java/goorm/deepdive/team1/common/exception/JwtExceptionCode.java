package goorm.deepdive.team1.common.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtExceptionCode implements ExceptionCode {
    EMPTY_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 없습니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "만료된 리프레시 토큰입니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
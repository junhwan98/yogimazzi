package goorm.deepdive.team1.api.jwt.exception;

import goorm.deepdive.team1.common.exception.ExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum JwtExceptionCode implements ExceptionCode {

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_TOKEN", "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "UNSUPPORTED_TOKEN", "지원되지 않는 JWT 토큰입니다."),
    MALFORMED_TOKEN(HttpStatus.BAD_REQUEST, "MALFORMED_TOKEN", "잘못된 형식의 JWT 토큰입니다."),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "INVALID_SIGNATURE", "JWT 서명이 유효하지 않습니다."),
    EMPTY_TOKEN(HttpStatus.BAD_REQUEST, "EMPTY_TOKEN", "JWT 토큰이 비어있거나 잘못되었습니다."),
    REFRESH_EMPTY_TOKEN(HttpStatus.BAD_REQUEST, "EMPTY_TOKEN", "Refresh 토큰이 비어있거나 서버의 토큰과 일치하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    JwtExceptionCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
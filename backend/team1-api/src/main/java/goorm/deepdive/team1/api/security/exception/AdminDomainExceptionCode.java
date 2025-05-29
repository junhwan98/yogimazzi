package goorm.deepdive.team1.api.security.exception;

import org.springframework.http.HttpStatus;

import goorm.deepdive.team1.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum AdminDomainExceptionCode implements ExceptionCode {
    ADMIN_NOT_FOUND(NOT_FOUND, "해당 관리자를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
package goorm.deepdive.team1.common.exception;

import static goorm.deepdive.team1.common.exception.GlobalExceptionCode.INVALID_INPUT;

public class InvalidInputException extends CustomException {
    public InvalidInputException() {
        super(INVALID_INPUT);
    }
}

package goorm.deepdive.team1.domain.user.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import goorm.deepdive.team1.common.exception.InvalidInputException;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum Gender {
    MALE("남자"),
    FEMALE("여자");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Gender from(String value) {
        return Arrays.stream(Gender.values())
                .filter(gender -> gender.value.equals(value))
                .findFirst()
                .orElseThrow(InvalidInputException::new);
    }
}
package goorm.deepdive.team1.domain.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgeGroups {
	TEENS("10.0-20.0"),
	TWENTIES("20.0-30.0"),
	THIRTIES("30.0-40.0"),
	FORTIES("40.0-50.0"),
	FIFTIES("50.0-60.0"),
	SIXTIES_AND_ABOVE("60.0-*");

	private final String description;
}


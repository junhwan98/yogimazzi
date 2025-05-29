package goorm.deepdive.team1.domain.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	SUPER("최고 관리자"),
	NORMAL("일반 관리자"),
	;

	private final String description;
}

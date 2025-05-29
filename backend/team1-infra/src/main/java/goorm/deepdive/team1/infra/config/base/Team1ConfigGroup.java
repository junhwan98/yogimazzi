package goorm.deepdive.team1.infra.config.base;

import goorm.deepdive.team1.infra.config.jpa.JpaAuditingConfig;
import goorm.deepdive.team1.infra.config.jpa.JpaConfig;
import goorm.deepdive.team1.infra.config.properties.PropertiesConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Team1ConfigGroup {
	JPA(JpaConfig.class),
	JPA_AUDITING(JpaAuditingConfig.class),
	PROPERTIES(PropertiesConfig.class);

	private final Class<? extends Team1Config> configClass;

}

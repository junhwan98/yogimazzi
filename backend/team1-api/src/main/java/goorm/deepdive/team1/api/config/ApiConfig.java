package goorm.deepdive.team1.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import goorm.deepdive.team1.infra.config.base.EnableTeam1Config;
import goorm.deepdive.team1.infra.config.base.Team1ConfigGroup;

@Configuration(proxyBeanMethods = false)
@ComponentScan("goorm.deepdive.team1")
@EnableTeam1Config({
	Team1ConfigGroup.JPA,
	Team1ConfigGroup.JPA_AUDITING,
	Team1ConfigGroup.PROPERTIES
})
class ApiConfig {
}

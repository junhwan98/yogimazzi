package goorm.deepdive.team1.infra.config.jpa;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import goorm.deepdive.team1.infra.config.base.Team1Config;

@EnableJpaAuditing
public class JpaAuditingConfig implements Team1Config {
}

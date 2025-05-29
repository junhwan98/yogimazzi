package goorm.deepdive.team1.infra.config.properties;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import goorm.deepdive.team1.infra.config.base.Team1Config;

@ConfigurationPropertiesScan(basePackages = "goorm.deepdive.team1.api")
public class PropertiesConfig implements Team1Config {
}


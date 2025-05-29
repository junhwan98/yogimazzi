package goorm.deepdive.team1.infra.config.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(Team1ConfigImportSelector.class)
public @interface EnableTeam1Config {
	Team1ConfigGroup[] value();
}

package goorm.deepdive.team1.infra.config.base;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

class Team1ConfigImportSelector implements DeferredImportSelector {
	@Override
	public String[] selectImports(AnnotationMetadata metadata) {
		return Arrays.stream(getValues(metadata))
			.map(v -> v.getConfigClass().getName())
			.toArray(String[]::new);
	}

	private Team1ConfigGroup[] getValues(AnnotationMetadata metadata) {
		Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableTeam1Config.class.getName());
		return (Team1ConfigGroup[])MapUtils.getObject(attributes, "value", new Team1ConfigGroup[] {});
	}
}

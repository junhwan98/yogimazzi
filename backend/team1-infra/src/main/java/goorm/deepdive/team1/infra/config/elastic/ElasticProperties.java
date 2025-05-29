package goorm.deepdive.team1.infra.config.elastic;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.elasticsearch")
public class ElasticProperties {
	private String uris;
	private String username;
	private String password;
}

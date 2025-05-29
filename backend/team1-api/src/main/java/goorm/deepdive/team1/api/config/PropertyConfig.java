package goorm.deepdive.team1.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {
        "classpath:env.properties", // env.properties 파일 소스 등록
        "file:/app/env.properties"
}, ignoreResourceNotFound = true)

public class PropertyConfig {

}
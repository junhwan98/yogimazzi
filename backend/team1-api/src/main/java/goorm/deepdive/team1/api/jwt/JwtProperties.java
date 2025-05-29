package goorm.deepdive.team1.api.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {
    private String secret;
    private long expirationSeconds;
    private String refreshSecret;
    private long refreshExpirationSeconds;

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setExpirationSeconds(long expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
    }

    public void setRefreshSecret(String refreshSecret) {
        this.refreshSecret = refreshSecret;
    }

    public void setRefreshExpirationSeconds(long refreshExpirationSeconds) {
        this.refreshExpirationSeconds = refreshExpirationSeconds;
    }
}
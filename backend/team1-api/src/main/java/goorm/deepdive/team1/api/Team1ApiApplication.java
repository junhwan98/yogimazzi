package goorm.deepdive.team1.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Team1ApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(Team1ApiApplication.class, args);
	}
}

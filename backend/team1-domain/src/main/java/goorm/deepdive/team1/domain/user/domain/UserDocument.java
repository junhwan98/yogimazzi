package goorm.deepdive.team1.domain.user.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@Document(indexName = "user_address")
@AllArgsConstructor
public class UserDocument {
	@Id
	private String userId;

	private String name;

	private String email;

	private String phoneNumber;

	private double x;

	private double y;

	private String regionAddress;

	private String roadAddress;

	private String region;

	private String gender;

	private Integer age;

	public void update(String name, String regionAddress, String roadAddress, String region, String gender, Integer age) {
		this.name = name;
		this.regionAddress = regionAddress;
		this.roadAddress = roadAddress;
		this.region = region;
		this.gender = gender;
		this.age = age;
	}

	public static UserDocument from(User user) {
		return UserDocument.builder()
			.userId(user.getId().toString())
			.name(user.getName())
			.email(user.getEmail())
			.phoneNumber(user.getPhoneNumber())
			.roadAddress(user.getAddress().getRoadAddress())
			.regionAddress(user.getAddress().getRegionAddress())
			.region(user.getAddress().getRegion())
			.gender(user.getGender().getValue())
			.age(user.getAge())
			.x(user.getAddress().getX())
			.y(user.getAddress().getY())
			.build();
	}

	public static UserDocument create(Long id, String name, String email, String phoneNumber, String regionAddress, String roadAddress, String region, String gender, Integer age) {
		return UserDocument.builder()
				.userId(String.valueOf(id))
				.name(name)
				.email(email)
				.phoneNumber(phoneNumber)
				.regionAddress(regionAddress)
				.roadAddress(roadAddress)
				.region(region)
				.gender(gender)
				.age(age)
				.build();
	}
}

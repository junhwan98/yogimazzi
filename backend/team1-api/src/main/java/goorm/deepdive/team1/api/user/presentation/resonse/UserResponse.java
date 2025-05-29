package goorm.deepdive.team1.api.user.presentation.resonse;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserResponse(
	@Schema(description = "이름", example = "박민준", requiredMode = REQUIRED)
	@NotBlank
	String name,

	@Schema(description = "이메일", example = "alswns11346@kgu.ac.kr", requiredMode = REQUIRED)
	@NotBlank
	String email,

	@Schema(description = "휴대폰 번호", example = "01012345678", requiredMode = REQUIRED)
	@NotBlank
	String phoneNumber,

	@Schema(description = "성별", example = "남자", requiredMode = REQUIRED)
	@NotNull
	Gender gender,

	@Schema(description = "나이", example = "20", requiredMode = REQUIRED)
	@NotNull
	Integer age
) {
	public static UserResponse from(User user) {
		return UserResponse.builder()
			.name(user.getName())
			.email(user.getEmail())
			.phoneNumber(user.getPhoneNumber())
			.gender(user.getGender())
			.age(user.getAge())
			.build();
	}
}

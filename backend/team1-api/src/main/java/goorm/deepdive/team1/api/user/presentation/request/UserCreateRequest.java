package goorm.deepdive.team1.api.user.presentation.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import goorm.deepdive.team1.common.valid.PhoneNumber;
import goorm.deepdive.team1.domain.user.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
	@Schema(description = "이름", example = "박민준", requiredMode = REQUIRED)
	@NotBlank
	String name,

	@Schema(description = "이메일", example = "alswns11346@kgu.ac.kr", requiredMode = REQUIRED)
	@Email
	@NotBlank
	String email,

	@Schema(description = "휴대폰 번호", example = "01012345678", requiredMode = REQUIRED)
	@PhoneNumber
	@NotBlank
	String phoneNumber,

	@Schema(description = "지번 주소", example = "경기 수원시 장안구 연무동 56-45", requiredMode = REQUIRED)
	@NotBlank
	String regionAddress,

	@Schema(description = "도로명 주소", example = "경기 수원시 장안구 창훈로52번길 22", requiredMode = REQUIRED)
	@NotBlank
	String roadAddress,

	@Schema(description = "성별", example = "남자", requiredMode = REQUIRED)
	@NotNull
	Gender gender,

	@Schema(description = "나이", example = "20", requiredMode = REQUIRED)
	@NotNull
	Integer age
) {
}

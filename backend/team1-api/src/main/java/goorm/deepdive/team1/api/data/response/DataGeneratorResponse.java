package goorm.deepdive.team1.api.data.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DataGeneratorResponse(
	@Schema(description = "걸린 시간", example = "총 1000000개의 데이터가 32500ms 만에 성공적으로 생성되었습니다", requiredMode = REQUIRED)
	@NotBlank
	String result
) {
	public static DataGeneratorResponse of(int totalRecords, Long duration) {
		return DataGeneratorResponse.builder()
			.result("총 " + totalRecords + "개의 데이터가 " + duration + "ms 만에 성공적으로 생성되었습니다.")
			.build();
	}
}

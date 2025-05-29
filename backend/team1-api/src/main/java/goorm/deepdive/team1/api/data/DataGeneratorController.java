package goorm.deepdive.team1.api.data;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import goorm.deepdive.team1.api.data.response.DataGeneratorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "DUMMY DATA", description = "더미 데이터 생성 API")
public interface DataGeneratorController {
	@Operation(summary = "주소 추가 API", description = """
			- Description : 이 API는 주소 데이터를 추가할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "200",
		content = @Content(schema = @Schema(implementation = DataGeneratorResponse.class))
	)
	ResponseEntity<DataGeneratorResponse> create(
		@Valid @RequestParam int totalRecords
	) throws IOException;

}

package goorm.deepdive.team1.api.user.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import goorm.deepdive.team1.api.addresshistory.presentation.response.AddressHistoryListResponse;
import goorm.deepdive.team1.api.paging.PaginatedListResponse;
import goorm.deepdive.team1.api.user.presentation.request.UserCreateRequest;
import goorm.deepdive.team1.api.user.presentation.request.UserUpdateRequest;
import goorm.deepdive.team1.api.user.presentation.resonse.UserHeatMapListResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserListResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserPersistResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserStatsResponse;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.domain.enums.AgeGroups;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Tag(name = "USER", description = "유저 API")
public interface UserController {

	@Operation(summary = "유저 추가 API", description = """
			- Description : 이 API는 유저 데이터를 추가할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "201",
		content = @Content(schema = @Schema(implementation = UserPersistResponse.class))
	)
	ResponseEntity<UserPersistResponse> create(
		@Valid @RequestBody UserCreateRequest request
	);

	@Operation(summary = "유저 상세 조회 API", description = """
			- Description : 이 API는 유저 데이터를 조회할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "200",
		content = @Content(schema = @Schema(implementation = UserCache.class))
	)
	ResponseEntity<UserCache> getUserCacheById(
		@Parameter(description = "조회할 사용자의 ID", example = "1")
		@PathVariable Long id
	);

	@Operation(summary = "유저 목록 조회 API", description = """
			- Description : 이 API는 유저 데이터 목록을 조회할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "200",
		content = @Content(schema = @Schema(implementation = UserListResponse.class))
	)
	ResponseEntity<PaginatedListResponse> getAll(
		@Parameter(
			description = "페이지 인덱스",
			example = "1",
			required = true
		) @Positive @RequestParam(defaultValue = "1") int page,
		@Parameter(
			description = "응답 개수",
			example = "10",
			required = true
		) @Positive @RequestParam(defaultValue = "10") int size
	);

	@Operation(summary = "유저 수정 API", description = """
			- Description : 이 API는 유저 데이터를 수정할 수 있습니다.
		""")
	@ApiResponse(responseCode = "204")
	ResponseEntity<Void> update(
		@Parameter(description = "수정할 사용자의 ID", example = "1")
		@PathVariable Long id,

		@Valid
		@RequestBody
		UserUpdateRequest request
	);

	@Operation(summary = "유저 삭제 API", description = """
			- Description : 이 API는 유저 데이터를 삭제할 수 있습니다.
		""")
	@ApiResponse(responseCode = "204")
	ResponseEntity<Void> delete(
		@Parameter(description = "삭제할 사용자의 ID", example = "1")
		@PathVariable Long id
	);

	@Operation(summary = "도로명 주소 기반 유저 목록 검색 API", description = """
			- Description : 이 API는 해당 키워드를 포함한 주소를 가지고 있는 유저 목록을 조회할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "200",
		content = @Content(schema = @Schema(implementation = PaginatedListResponse.class))
	)
	ResponseEntity<PaginatedListResponse> searchUsersByRoadAddressKeyword(
		@Parameter(
			description = "페이지 인덱스",
			example = "1",
			required = true
		) @Positive @RequestParam(defaultValue = "1") int page,
		@Parameter(
			description = "응답 개수",
			example = "10",
			required = true
		) @Positive @RequestParam(defaultValue = "10") int size,
		@Parameter(
			description = "검색할 주소 키워드",
			example = "서울"
		)
		@RequestParam(required = false)
		String keyword
	);

	@Operation(summary = "지번 주소 기반 유저 목록 검색 API", description = """
			- Description : 이 API는 해당 키워드를 포함한 주소를 가지고 있는 유저 목록을 조회할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "200",
		content = @Content(schema = @Schema(implementation = PaginatedListResponse.class))
	)
	ResponseEntity<PaginatedListResponse> searchUsersByRegionAddressKeyword(
		@Parameter(
			description = "페이지 인덱스",
			example = "1",
			required = true
		) @Positive @RequestParam(defaultValue = "1") int page,
		@Parameter(
			description = "응답 개수",
			example = "10",
			required = true
		) @Positive @RequestParam(defaultValue = "10") int size,
		@Parameter(
			description = "검색할 주소 키워드",
			example = "충북"
		)
		@RequestParam(required = false)
		String keyword
	);

	@Operation(summary = "사용자 이름 기반 유저 목록 검색 API", description = """
			- Description : 이 API는 해당 사용자 이름을 기반으로 유저 목록을 조회할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "200",
		content = @Content(schema = @Schema(implementation = PaginatedListResponse.class))
	)
	ResponseEntity<PaginatedListResponse> searchUsersByName(
		@Parameter(
			description = "페이지 인덱스",
			example = "1",
			required = true
		) @PositiveOrZero @RequestParam(defaultValue = "1") int page,
		@Parameter(
			description = "응답 개수",
			example = "10",
			required = true
		) @Positive @RequestParam(defaultValue = "10") int size,
		@Parameter(
			description = "검색할 이름",
			example = "홍길동"
		)
		@RequestParam(required = false)
		String name
	);

	@Operation(summary = "사용자 통계 API", description = """
			- Description : 이 API는 사용자 통계 데이터를 조회할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "200",
		content = @Content(schema = @Schema(implementation = UserStatsResponse.class))
	)
	ResponseEntity<UserStatsResponse> searchStats(
		@Parameter(
			description = "성별",
			example = "[MALE, FEMALE]"
		) @RequestParam(required = false) List<String> gender,
		@Parameter(
			description = "지역",
			example = "[\"서울\", \"부산\"]"
		) @RequestParam(required = false) List<String> region,
		@Parameter(
			description = "연령대",
			example = "[TEEN, TWENTIES]"
		) @RequestParam(required = false) List<AgeGroups> ageGroups
	);

	@Operation(summary = "사용자 히트맵 API", description = """
			- Description : 이 API는 사용자 히트맵 데이터를 조회할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "200",
		content = @Content(schema = @Schema(implementation = UserHeatMapListResponse.class))
	)
	ResponseEntity<UserHeatMapListResponse> searchHeatMap(
		@Parameter(
			description = "지역",
			example = "[\"서울\", \"부산\"]"
		) @RequestParam(required = false) List<String> region,
		@Parameter(
			description = "연령대",
			example = "[TEEN, TWENTIES]"
		) @RequestParam(required = false) List<AgeGroups> ageGroups
	);

	@Operation(summary = "유저 주소 이력 조회 API", description = """
			- Description : 이 API는 유저 주소 이력을 조회할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "200",
		content = @Content(schema = @Schema(implementation = AddressHistoryListResponse.class))
	)
	ResponseEntity<AddressHistoryListResponse> getAddressHistoriesByUserId(
		@Parameter(description = "조회할 사용자의 ID", example = "1")
		@PathVariable Long id
	);
}

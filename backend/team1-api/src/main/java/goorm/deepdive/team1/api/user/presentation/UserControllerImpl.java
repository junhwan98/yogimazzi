package goorm.deepdive.team1.api.user.presentation;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import goorm.deepdive.team1.api.addresshistory.Application.AddressHistoryFacade;
import goorm.deepdive.team1.api.addresshistory.presentation.response.AddressHistoryListResponse;
import goorm.deepdive.team1.api.paging.PaginatedListResponse;
import goorm.deepdive.team1.api.user.application.UserFacade;
import goorm.deepdive.team1.api.user.presentation.request.UserCreateRequest;
import goorm.deepdive.team1.api.user.presentation.request.UserUpdateRequest;
import goorm.deepdive.team1.api.user.presentation.resonse.UserHeatMapListResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserHeatMapResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserPersistResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserStatsResponse;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.domain.user.domain.enums.AgeGroups;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController{
	private final UserFacade userFacade;
	private final AddressHistoryFacade addressHistoryFacade;

	@Override
	@PostMapping
	public ResponseEntity<UserPersistResponse> create(UserCreateRequest request) {
		UserPersistResponse response = userFacade.create(request);
		return ResponseEntity.status(CREATED).body(response);
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<UserCache> getUserCacheById(Long id) {
		UserCache response = userFacade.getUserCacheById(id);
		return ResponseEntity.ok(response);
	}

	@Override
	@GetMapping
	public ResponseEntity<PaginatedListResponse> getAll(
		int page,
		int size
	) {
		PaginatedListResponse response = userFacade.getAll(PageRequest.of(page - 1, size));
		return ResponseEntity.ok(response);
	}

	@Override
	@PatchMapping("/{id}")
	public ResponseEntity<Void> update(Long id, UserUpdateRequest request) {
		userFacade.update(id, request);
		return ResponseEntity.noContent().build();
	}

	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(Long id) {
		userFacade.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@GetMapping("/search/road")
	public ResponseEntity<PaginatedListResponse> searchUsersByRoadAddressKeyword(
		int page,
		int size,
		String keyword
	) {
		PaginatedListResponse response = userFacade.searchUsersByRoadAddressKeyword(
			keyword,
			PageRequest.of(page - 1, size)
		);
		return ResponseEntity.ok(response);
	}

	@Override
	@GetMapping("/search/region")
	public ResponseEntity<PaginatedListResponse> searchUsersByRegionAddressKeyword(
		int page,
		int size,
		String keyword
	) {
		PaginatedListResponse response = userFacade.searchUsersByRegionAddressKeyword(
			keyword,
			PageRequest.of(page - 1, size)
		);
		return ResponseEntity.ok(response);
	}

	@Override
	@GetMapping("/search/name")
	public ResponseEntity<PaginatedListResponse> searchUsersByName(
		int page,
		int size,
		String name
	) {
		PaginatedListResponse response = userFacade.searchUsersByName(
			name,
			PageRequest.of(page - 1, size)
		);
		return ResponseEntity.ok(response);
	}

	@Override
	@GetMapping("/stats")
	public ResponseEntity<UserStatsResponse> searchStats(
		List<String> gender,
		List<String> region,
		List<AgeGroups> ageGroups)
	{
		UserStatsResponse response = userFacade.getUserStatistics(gender, region, ageGroups);
		return ResponseEntity.ok(response);
	}

	@Override
	@GetMapping("/heatmap")
	public ResponseEntity<UserHeatMapListResponse> searchHeatMap(List<String> region, List<AgeGroups> ageGroups) {
		List<UserDocument> searchResponse = userFacade.getUserHeatMap(region, ageGroups);
		List<UserHeatMapResponse> responseList = searchResponse.stream()
			.map(UserHeatMapResponse::from)
			.toList();

		UserHeatMapListResponse response = UserHeatMapListResponse.of(responseList);
		return ResponseEntity.ok(response);
	}

	@Override
	@GetMapping("/{id}/address-histories")
	public ResponseEntity<AddressHistoryListResponse> getAddressHistoriesByUserId(Long id) {
		AddressHistoryListResponse response = addressHistoryFacade
			.getAddressHistories(id);

		return ResponseEntity.ok(response);
	}
}

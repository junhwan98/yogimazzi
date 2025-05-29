package goorm.deepdive.team1.api.paging;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Builder;

@Builder
public record PaginatedListResponse<T>(
	List<T> contents,
	PageableResponse pageable
) {
	public static <T> PaginatedListResponse<T> from(Page<T> contents) {
		return PaginatedListResponse.<T>builder()
			.contents(contents.getContent())
			.pageable(PageableResponse.of(contents.getPageable(), contents.getTotalElements()))
			.build();
	}
}

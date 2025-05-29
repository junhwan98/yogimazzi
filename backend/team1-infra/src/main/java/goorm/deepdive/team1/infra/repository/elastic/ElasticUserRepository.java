package goorm.deepdive.team1.infra.repository.elastic;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.aggregations.MultiBucketBase;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.domain.user.domain.enums.AgeGroups;
import goorm.deepdive.team1.infra.repository.elastic.exception.ElasticQueryExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ElasticUserRepository {
	private final ElasticsearchClient elasticsearchClient;
	private static final String INDEX_NAME = "user_address";

	public void deleteScheduling(List<Long> ids) {
		try {
			List<BulkOperation> deleteOperations = ids.stream()
				.map(id -> BulkOperation.of(op -> op
					.delete(d -> d.index(INDEX_NAME).id(String.valueOf(id)))
				))
				.collect(Collectors.toList());

			BulkRequest bulkRequest = BulkRequest.of(b -> b.operations(deleteOperations));
			elasticsearchClient.bulk(bulkRequest);
		} catch (Exception e) {
			throw new ElasticQueryExecutionException();
		}
	}
	public Page<UserDocument> searchByRoadAddress(String roadAddress, Pageable pageable){
		return search("roadAddress", roadAddress, pageable);
	}

	public Page<UserDocument> searchByRegionAddress(String regionAddress, Pageable pageable){
		return search("regionAddress", regionAddress, pageable);
	}

	public Page<UserDocument> searchByName(String name, Pageable pageable){
		return search("name", name, pageable);
	}

	private Page<UserDocument> search(String field, String keyword, Pageable pageable){
		Query query = Query.of(q -> q
			.match(m -> m
				.field(field)
				.query(keyword)
			)
		);
		SearchResponse<UserDocument> response;

		try {
			response = elasticsearchClient.search(s -> s
					.index(INDEX_NAME)
					.query(query)
					.from((int) pageable.getOffset())
					.size(pageable.getPageSize()),
				UserDocument.class
			);
		} catch (Exception e) {
			throw new ElasticQueryExecutionException();
		}

		List<UserDocument> userList = response.hits().hits().stream()
			.map(Hit::source)
			.collect(Collectors.toList());

		long totalHits = response.hits().total() != null
			? response.hits().total().value()
			: userList.size();

		return new PageImpl<>(userList, pageable, totalHits);
	}

	public void save(UserDocument userDocument) {
		try {
			elasticsearchClient.index(IndexRequest.of(i -> i
				.index(INDEX_NAME)
				.id(userDocument.getUserId())
				.document(userDocument)
			));
		} catch (Exception e) {
			throw new ElasticQueryExecutionException();
		}
	}

	public List<UserDocument> searchHeatMap(List<String> regions, List<AgeGroups> ageGroups){
		try {
			SearchResponse<UserDocument> response = elasticsearchClient.search(s -> s
					.index(INDEX_NAME)
					.size(10_000)
					.query(q -> q
						.bool(b -> b
							.must(getRegionQuery(regions))
							.must(getAgeRangeQuery(ageGroups))
						)
					)
				, UserDocument.class
			);

			return response.hits().hits().stream()
				.map(Hit::source)
				.toList();
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new ElasticQueryExecutionException();
		}
	}
	public Map<String, Object> searchUserStatistics(List<String> genders, List<String> regions, List<AgeGroups> ageGroups) {
		try {
			SearchResponse<Void> response = elasticsearchClient.search(s -> s
					.index(INDEX_NAME)
					.size(0)
					.query(q -> q
						.bool(b -> b
							.must(getGenderQuery(genders))
							.must(getRegionQuery(regions))
							.must(getAgeRangeQuery(ageGroups))
						)
					)
					.aggregations("genderStats", a -> a.terms(t -> t.field("gender")))
					.aggregations("regionStats", a -> a.terms(t -> t.field("region")))
					.aggregations("ageStats", a -> a.range(r -> r.field("age").ranges(getAgeBuckets(ageGroups))))
				, Void.class);

			long total = response.hits().total() != null ? response.hits().total().value() : 0;

			Map<String, Long> genderStats = fillMissingKeys(extractTermsAggregation(response, "genderStats"), genders);
			Map<String, Long> regionStats = fillMissingKeys(extractTermsAggregation(response, "regionStats"), regions);
			List<String> ageGroupStrings = ageGroups.stream()
				.map(AgeGroups::getDescription)
				.toList();
			Map<String, Long> ageStats = fillMissingKeys(extractRangeAggregation(response.aggregations().get("ageStats")), ageGroupStrings);

			return Map.of(
				"total", total,
				"genderStats", genderStats,
				"regionStats", regionStats,
				"ageStats", ageStats
			);
		} catch (Exception e) {
			throw new ElasticQueryExecutionException();
		}
	}

	private Query getGenderQuery(List<String> genders) {
		if (genders == null || genders.isEmpty()) return Query.of(q -> q.matchAll(m -> m));
		return Query.of(q -> q.terms(t -> t
			.field("gender")
			.terms(v -> v.value(genders.stream()
				.map(FieldValue::of)
				.toList()
			))
		));
	}

	private Query getRegionQuery(List<String> regions) {
		if (regions == null || regions.isEmpty()) return Query.of(q -> q.matchAll(m -> m));
		return Query.of(q -> q.terms(t -> t
			.field("region")
			.terms(v -> v.value(regions.stream()
				.map(FieldValue::of)
				.toList()
			))
		));
	}


	public Query getAgeRangeQuery(List<AgeGroups> ageGroups) {
		return Query.of(q -> q.bool(b -> b
			.should(ageGroups.stream()
				.map(this::getAgeRangeQueryForGroup)
				.collect(Collectors.toList()))
			.minimumShouldMatch(String.valueOf(1))
		));
	}

	private Query getAgeRangeQueryForGroup(AgeGroups ageGroup) {
		NumberRangeQuery.Builder rangeQuery = new NumberRangeQuery.Builder().field("age");
		switch (ageGroup) {
			case TEENS:
				rangeQuery.gte(10.0).lt(20.0);
				break;
			case TWENTIES:
				rangeQuery.gte(20.0).lt(30.0);
				break;
			case THIRTIES:
				rangeQuery.gte(30.0).lt(40.0);
				break;
			case FORTIES:
				rangeQuery.gte(40.0).lt(50.0);
				break;
			case FIFTIES:
				rangeQuery.gte(50.0).lt(60.0);
				break;
			case SIXTIES_AND_ABOVE:
				rangeQuery.gte(60.0);
				break;
			default:
				throw new IllegalArgumentException(ageGroup.getDescription());
		}
		return Query.of(q -> q.range(r -> r.number(rangeQuery.build())));
	}

	private Map<String, Long> extractTermsAggregation(SearchResponse<Void> response, String aggName) {
		return response.aggregations().get(aggName).sterms().buckets().array().stream()
			.collect(Collectors.toMap(bucket -> bucket.key()._get().toString(), StringTermsBucket::docCount));
	}

	private Map<String, Long> extractRangeAggregation(Aggregate aggregation) {
		return aggregation.range().buckets().array().stream()
			.collect(Collectors.toMap(bucket -> bucket.key().toString(),
				MultiBucketBase::docCount));
	}

	private List<AggregationRange> getAgeBuckets(List<AgeGroups> ageGroups) {
		return ageGroups.stream()
			.map(this::getAgeBucket)
			.toList();
	}

	private AggregationRange getAgeBucket(AgeGroups ageGroup) {
		return switch (ageGroup) {
			case TEENS -> AggregationRange.of(r -> r.from(10.0).to(20.0));
			case TWENTIES -> AggregationRange.of(r -> r.from(20.0).to(30.0));
			case THIRTIES -> AggregationRange.of(r -> r.from(30.0).to(40.0));
			case FORTIES -> AggregationRange.of(r -> r.from(40.0).to(50.0));
			case FIFTIES -> AggregationRange.of(r -> r.from(50.0).to(60.0));
			case SIXTIES_AND_ABOVE -> AggregationRange.of(r -> r.from(60.0));
		};
	}

	private Map<String, Long> fillMissingKeys(Map<String, Long> originalData, List<String> requestedKeys) {
		if (requestedKeys == null) return originalData;
		return requestedKeys.stream()
			.collect(Collectors.toMap(
				key -> key,
				key -> originalData.getOrDefault(key, 0L)
			));
	}
}

package goorm.deepdive.team1.infra.data;

import static java.util.Locale.KOREA;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import net.datafaker.Faker;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.infra.repository.redis.RedisAddressHistoryRepository;
import goorm.deepdive.team1.infra.repository.redis.RedisUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BatchService {
	private final JdbcTemplate jdbcTemplate;
	private final RedisUserRepository redisUserRepository;
	private final RedisAddressHistoryRepository redisAddressHistoryRepository;
	private final ElasticsearchClient elasticsearchClient;

	private static final Faker faker = new Faker(KOREA);

	@Async("taskExecutor")
	public CompletableFuture<Void> insertBatchAsync(int batchSize){
		insertBatch(batchSize);
		return CompletableFuture.completedFuture(null);
	}

	public void insertBatch(int total){
		try {
			String insertUserSql = "INSERT INTO \"user\" (name, email, phone_number, address_id, gender, age, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
			String insertAddressSql = "INSERT INTO address (x, y, region_address, road_address, region, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
			String insertHistorySql = "INSERT INTO address_history (user_id, address_id, created_at, updated_at) VALUES (?, ?, ?, ?) RETURNING id";

			List<BulkOperation> bulkOperations = new ArrayList<>();

			for (int i = 0; i < total; i++) {
				String name = faker.name().fullName().replaceAll(" ","");
				String email = faker.bothify("??????####@example.com");
				String phoneNumber = faker.regexify("010[1-9]{8}");
				String gender = faker.options().option("MALE", "FEMALE");
				int age = faker.number().numberBetween(10, 80);

				double x = faker.number().randomDouble(6, 127, 129);
				double y = faker.number().randomDouble(6, 35, 38);
				String region = faker.address().state();

				String regionAddress = region + " " +
					faker.address().city() + " " +
					faker.address().streetName() + " " +
					faker.address().streetAddressNumber() + "-" +
					faker.address().streetAddressNumber();

				String roadAddress = region + " " +
					faker.address().city() + " " +
					faker.address().streetAddress() + "길 " +
					faker.address().streetAddressNumber();

				Long addressId = jdbcTemplate.queryForObject(insertAddressSql, new Object[]{
					x,
					y,
					regionAddress,
					roadAddress,
					region,
					Timestamp.valueOf(LocalDateTime.now()),
					Timestamp.valueOf(LocalDateTime.now())
				}, Long.class);

				Long userId = jdbcTemplate.queryForObject(insertUserSql, new Object[]{
					name,
					email,
					phoneNumber,
					addressId,
					gender,
					age,
					Timestamp.valueOf(LocalDateTime.now()),
					Timestamp.valueOf(LocalDateTime.now())
				}, Long.class);

				Long addressHistoryId = jdbcTemplate.queryForObject(insertHistorySql, new Object[]{
					userId,
					addressId,
					Timestamp.valueOf(LocalDateTime.now()),
					Timestamp.valueOf(LocalDateTime.now())
				}, Long.class);;

				UserCache userCache = UserCache
					.create(userId, name, email, phoneNumber, regionAddress, roadAddress, gender, age);
				redisUserRepository.save(userCache);

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
				String createdAt = LocalDateTime.now().format(formatter);
				AddressHistoryCache addressHistoryCache = AddressHistoryCache
					.create(addressHistoryId, userId, regionAddress, roadAddress, createdAt);
				redisAddressHistoryRepository.save(addressHistoryCache);

				UserDocument userDoc = UserDocument.builder()
					.userId(userId.toString())
					.name(name)
					.email(email)
					.phoneNumber(phoneNumber)
					.region(region)
					.regionAddress(regionAddress)
					.roadAddress(roadAddress)
					.age(age)
					.gender(gender)
					.x(x)
					.y(y)
					.build();

				bulkOperations.add(new BulkOperation.Builder()
					.index(e -> e
						.index("user_address")
						.id(userDoc.getUserId())
						.document(userDoc))
					.build());
			}

			BulkRequest bulkRequest = new BulkRequest.Builder()
				.operations(bulkOperations)
				.build();

			BulkResponse response = elasticsearchClient.bulk(bulkRequest);

			if (response.errors()) {
				System.out.println("Bulk 인덱싱 중 오류 발생");
			} else {
				System.out.println("Bulk 인덱싱 성공!");
			}
			System.out.println(total + " 개 성공!");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}

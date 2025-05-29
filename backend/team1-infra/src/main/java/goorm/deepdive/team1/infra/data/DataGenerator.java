package goorm.deepdive.team1.infra.data;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataGenerator {
	private final BatchService batchService;
	private static final int BATCH_SIZE = 1000;

	public Long generateData(int totalRecords) {
		long startTime = System.currentTimeMillis();

		int totalBatches = totalRecords / BATCH_SIZE;
		int remainingRecords = totalRecords % BATCH_SIZE;

		CompletableFuture<?>[] futures = IntStream.range(0, totalBatches)
			.mapToObj(i -> batchService.insertBatchAsync(BATCH_SIZE))
			.toArray(CompletableFuture[]::new);

		CompletableFuture.allOf(futures).join();

		if (remainingRecords > 0) {
			batchService.insertBatchAsync(remainingRecords).join();
		}

		long endTime = System.currentTimeMillis();
		return endTime - startTime;
	}
}

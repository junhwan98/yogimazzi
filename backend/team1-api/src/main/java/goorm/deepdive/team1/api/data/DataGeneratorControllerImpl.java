package goorm.deepdive.team1.api.data;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import goorm.deepdive.team1.api.data.response.DataGeneratorResponse;
import goorm.deepdive.team1.infra.data.DataGenerator;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/data-generator")
@RequiredArgsConstructor
public class DataGeneratorControllerImpl implements DataGeneratorController {
	private final DataGenerator dataGenerator;

	@Override
	@PostMapping
	public ResponseEntity<DataGeneratorResponse> create(int totalRecords) {
		Long duration = dataGenerator.generateData(totalRecords);
		DataGeneratorResponse response = DataGeneratorResponse.of(totalRecords * 3, duration);
		return ResponseEntity.ok(response);
	}
}

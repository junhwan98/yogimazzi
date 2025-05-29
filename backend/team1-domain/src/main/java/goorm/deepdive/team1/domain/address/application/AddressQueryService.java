package goorm.deepdive.team1.domain.address.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.address.infrastructure.AddressRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressQueryService {
	private final AddressRepository addressRepository;

	public Address findByRoadAddress(String roadAddress) {
		return addressRepository.findByRoadAddressContainingAndDeletedAtIsNull(roadAddress)
			.orElse(null);
	}
}

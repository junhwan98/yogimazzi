package goorm.deepdive.team1.domain.address.application;

import org.springframework.stereotype.Service;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.address.infrastructure.AddressRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressCommandService {
	private final AddressRepository addressRepository;

	public Address save(Address address) {
		return addressRepository.save(address);
	}
}

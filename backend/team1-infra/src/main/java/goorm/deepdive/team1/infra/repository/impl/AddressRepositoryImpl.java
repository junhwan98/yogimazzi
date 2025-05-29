package goorm.deepdive.team1.infra.repository.impl;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.address.infrastructure.AddressRepository;
import goorm.deepdive.team1.infra.repository.jpa.JpaAddressRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryImpl implements AddressRepository {
	private final JpaAddressRepository jpaAddressRepository;

	@Override
	public Address save(Address address) {
		return jpaAddressRepository.save(address);
	}

	@Override
	public Optional<Address> findByRoadAddressContainingAndDeletedAtIsNull(String roadAddress) {
		return jpaAddressRepository.findByRoadAddressContainingAndDeletedAtIsNull(roadAddress);
	}
}

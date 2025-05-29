package goorm.deepdive.team1.domain.address.infrastructure;

import java.util.Optional;

import goorm.deepdive.team1.domain.address.domain.Address;

public interface AddressRepository {
	Address save(Address address);

	Optional<Address> findByRoadAddressContainingAndDeletedAtIsNull(String roadAddress);
}

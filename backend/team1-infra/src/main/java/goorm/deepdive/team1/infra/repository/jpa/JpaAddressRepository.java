package goorm.deepdive.team1.infra.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import goorm.deepdive.team1.domain.address.domain.Address;

public interface JpaAddressRepository extends JpaRepository<Address, Long> {
	List<Address> findAllByDeletedAtIsNull();

	Optional<Address> findByIdAndDeletedAtIsNull(Long id);

	@Query("SELECT a FROM Address a WHERE a.roadAddress LIKE %:roadAddress% AND a.deletedAt IS NULL")
	Optional<Address> findByRoadAddressContainingAndDeletedAtIsNull(@Param("roadAddress") String roadAddress);
}

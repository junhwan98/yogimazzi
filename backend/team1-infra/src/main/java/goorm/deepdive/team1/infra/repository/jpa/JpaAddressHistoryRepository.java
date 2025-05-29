package goorm.deepdive.team1.infra.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;

public interface JpaAddressHistoryRepository extends JpaRepository<AddressHistory, Long> {
	Optional<AddressHistory> findByIdAndDeletedAtIsNull(Long id);

	List<AddressHistory> findAllByDeletedAtIsNull();

	List<AddressHistory> findByUserIdAndDeletedAtIsNull(Long userId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM AddressHistory ah WHERE ah.user.id IN :userIds")
	void deleteScheduling(@Param("userIds") List<Long> userIds);
}

package goorm.deepdive.team1.infra.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import goorm.deepdive.team1.domain.user.domain.User;

public interface JpaUserRepository extends JpaRepository<User, Long> {
	Page<User> findAllByDeletedAtIsNull(Pageable pageable);

	Optional<User> findByIdAndDeletedAtIsNull(Long id);

	boolean existsByEmail(String email);

	@Query("SELECT u.id FROM User u WHERE u.deletedAt IS NOT NULL")
	List<Long> findIdsByDeletedAtIsNotNull();

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM User u WHERE u.id IN :ids")
	void deleteUsersInBatch(@Param("ids") List<Long> ids);

}

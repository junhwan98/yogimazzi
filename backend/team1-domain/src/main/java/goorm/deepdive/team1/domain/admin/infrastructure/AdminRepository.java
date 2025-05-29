package goorm.deepdive.team1.domain.admin.infrastructure;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    Admin save(Admin admin);

    Optional<Admin> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Admin> findById(Long id);

    void delete(Admin admin);

    Page<Admin> findAll(Pageable pageable);
}

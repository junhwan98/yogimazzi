package goorm.deepdive.team1.infra.repository.impl;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.infrastructure.AdminRepository;
import goorm.deepdive.team1.infra.repository.jpa.JpaAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository {
    private final JpaAdminRepository jpaAdminRepository;

    @Override
    public Admin save(Admin admin) {
        return jpaAdminRepository.save(admin);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return jpaAdminRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaAdminRepository.existsByEmail(email);
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return jpaAdminRepository.findById(id);
    }

    @Override
    public void delete(Admin admin) {
        jpaAdminRepository.delete(admin);
    }

    @Override
    public Page<Admin> findAll(Pageable pageable) {
        return jpaAdminRepository.findAll(pageable);
    }
}
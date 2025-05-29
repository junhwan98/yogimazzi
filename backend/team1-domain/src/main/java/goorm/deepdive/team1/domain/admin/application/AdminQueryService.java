package goorm.deepdive.team1.domain.admin.application;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.exception.AdminEmailAlreadyExistsException;
import goorm.deepdive.team1.domain.admin.exception.AdminNotFoundException;
import goorm.deepdive.team1.domain.admin.infrastructure.AdminRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminQueryService {
    private final AdminRepository adminRepository;

    public void validateEmailUniqueness(String email) {
        Optional.ofNullable(email)
            .filter(adminRepository::existsByEmail)
            .ifPresent(e -> { throw new AdminEmailAlreadyExistsException(); });
    }

    public Page<Admin> getAdminsByPage(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(AdminNotFoundException::new);
    }

    public Admin getById(Long id) {
        return adminRepository.findById(id)
            .orElseThrow(AdminNotFoundException::new);
    }
}

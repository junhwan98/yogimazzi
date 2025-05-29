package goorm.deepdive.team1.api.security;

import goorm.deepdive.team1.api.security.exception.AdminNotFoundException;
import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.infrastructure.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(AdminNotFoundException::new);
        // Admin 엔티티를 CustomAdminDetails로 감싸서 반환
        return new CustomAdminDetails(admin);
    }
}
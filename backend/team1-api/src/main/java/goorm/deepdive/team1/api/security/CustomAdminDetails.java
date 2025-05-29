package goorm.deepdive.team1.api.security;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomAdminDetails implements UserDetails {

    private final Admin admin;

    public CustomAdminDetails(Admin admin) {
        this.admin = admin;
    }
    public Admin getAdmin() {
        return admin;
    }

    public Long getAdminId() {
        return admin.getId();
    }

    @Override
    public String getUsername() {
        return admin.getEmail();
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    // getAuthorities() 메서드를 구현하면, 별도의 getRole() 메서드를 만들지 않아도 역할(Role) 정보를 사용할 수 있음.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (admin.getRole() != null) {
            authorities.add(() -> admin.getRole().name());
        } else {
            // 기본 권한 설정 또는 로그 남기기
            authorities.add(() -> "ROLE_USER");
        }
        return authorities;
    }
    @Override
    public boolean isEnabled() {
        // 관리자의 활성 상태를 항상 true로 설정 (추후 필요시 조건 추가 가능)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 인증 정보가 만료되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않음
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않음
    }
}
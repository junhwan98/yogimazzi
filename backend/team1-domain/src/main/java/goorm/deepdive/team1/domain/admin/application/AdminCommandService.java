package goorm.deepdive.team1.domain.admin.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.domain.Role;
import goorm.deepdive.team1.domain.admin.exception.AdminPasswordMismatchException;
import goorm.deepdive.team1.domain.admin.exception.CannotChangeOwnRoleException;
import goorm.deepdive.team1.domain.admin.infrastructure.AdminRepository;
import goorm.deepdive.team1.domain.admin.security.PasswordEncryptor;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommandService {
    private final AdminRepository adminRepository;
    private final PasswordEncryptor passwordEncryptor;

    public Admin register(String email, String password, Role role) {
        Admin admin = Admin.create(email, passwordEncryptor.encode(password), role);
        return adminRepository.save(admin);
    }

    public void deleteAdmin(Admin admin) {
        adminRepository.delete(admin);
    }

    public void updatePassword(Admin admin, String oldPassword, String newPassword) {
        if (!passwordEncryptor.matches(oldPassword, admin.getPassword())) {
            throw new AdminPasswordMismatchException();
        }

        admin.updatePassword(passwordEncryptor.encode(newPassword));
        adminRepository.save(admin);
    }

    public void updateAdminRole(Admin admin, Role newRole, Long loggedInAdminId) {
        if (loggedInAdminId.equals(admin.getId())) {
            throw new CannotChangeOwnRoleException();
        }
        admin.updateRole(newRole);
    }

}
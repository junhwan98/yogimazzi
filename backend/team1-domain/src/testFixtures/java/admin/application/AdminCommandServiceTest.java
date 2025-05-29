package admin.application;

import goorm.deepdive.team1.domain.admin.application.AdminCommandService;
import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.domain.Role;
import goorm.deepdive.team1.domain.admin.exception.AdminPasswordMismatchException;
import goorm.deepdive.team1.domain.admin.exception.CannotChangeOwnRoleException;
import mock.repository.FakeAdminRepository;
import goorm.deepdive.team1.domain.admin.security.PasswordEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminCommandServiceTest {
    private AdminCommandService adminCommandService;
    private FakeAdminRepository fakeAdminRepository;
    private PasswordEncryptor passwordEncryptor;

    @BeforeEach
    void setUp() {
        fakeAdminRepository = new FakeAdminRepository();
        passwordEncryptor = mock(PasswordEncryptor.class);
        adminCommandService = new AdminCommandService(fakeAdminRepository, passwordEncryptor);
    }

    @Test
    @DisplayName("register는 새로운 관리자를 등록한다.")
    void 관리자_등록_테스트() {
        // given
        when(passwordEncryptor.encode("1234")).thenReturn("encryptedPassword");

        // when
        Admin admin = adminCommandService.register("goorm@goorm.com", "1234", Role.SUPER);

        // then
        assertThat(admin).isNotNull();
        assertThat(admin.getEmail()).isEqualTo("goorm@goorm.com");
        assertThat(admin.getPassword()).isEqualTo("encryptedPassword");
        assertThat(admin.getRole()).isEqualTo(Role.SUPER);
    }

    @Test
    @DisplayName("deleteAdmin은 관리자를 삭제한다.")
    void 관리자_삭제_테스트() {
        // given
        Admin admin = adminCommandService.register("super@goorm.com", "1234", Role.SUPER);
        assertThat(fakeAdminRepository.findById(admin.getId())).isPresent();

        // when
        adminCommandService.deleteAdmin(admin);

        // then
        assertThat(fakeAdminRepository.findById(admin.getId())).isEmpty();
    }

    @Test
    @DisplayName("updatePassword는 관리자의 비밀번호를 변경한다.")
    void 비밀번호_변경_테스트() {
        // given
        Admin admin = adminCommandService.register("super@goorm.com", "1234", Role.SUPER);
        when(passwordEncryptor.matches("1234", admin.getPassword())).thenReturn(true);
        when(passwordEncryptor.encode("4321")).thenReturn("encryptedNewPassword");

        // when
        adminCommandService.updatePassword(admin, "1234", "4321");

        // then
        assertThat(admin.getPassword()).isEqualTo("encryptedNewPassword");
    }

    @Test
    @DisplayName("updatePassword는 잘못된 기존 비밀번호 입력 시 예외를 발생시킨다.")
    void 비밀번호_변경_실패_테스트() {
        // given
        Admin admin = adminCommandService.register("super@goorm.com", "1234", Role.SUPER);
        when(passwordEncryptor.matches("1111", admin.getPassword())).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> adminCommandService.updatePassword(admin, "1111", "4321"))
                .isInstanceOf(AdminPasswordMismatchException.class);
    }

    @Test
    @DisplayName("최고 관리자는 다른 관리자의 권한을 변경할 수 있다.")
    void 최고관리자_권한_변경_테스트() {
        // given
        Admin superAdmin = adminCommandService.register("super@goorm.com", "1234", Role.SUPER);
        Admin normalAdmin = adminCommandService.register("normal@goorm.com", "1234", Role.NORMAL);

        // when
        adminCommandService.updateAdminRole(normalAdmin, Role.SUPER, superAdmin.getId());

        // then
        assertThat(normalAdmin.getRole()).isEqualTo(Role.SUPER);
    }

    @Test
    @DisplayName("일반 관리자는 자신의 권한을 변경할 수 없다.")
    void 일반관리자_자기권한_변경_실패_테스트() {
        // given
        Admin normalAdmin = adminCommandService.register("normal@goorm.com", "1234", Role.NORMAL);

        // when & then
        assertThatThrownBy(() -> adminCommandService.updateAdminRole(normalAdmin, Role.SUPER, normalAdmin.getId()))
                .isInstanceOf(CannotChangeOwnRoleException.class);
    }
}

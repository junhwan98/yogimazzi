package admin.application;

import goorm.deepdive.team1.domain.admin.application.AdminQueryService;
import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.domain.Role;
import goorm.deepdive.team1.domain.admin.exception.AdminEmailAlreadyExistsException;
import goorm.deepdive.team1.domain.admin.exception.AdminNotFoundException;
import mock.repository.FakeAdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class AdminQueryServiceTest {
    private AdminQueryService adminQueryService;
    private FakeAdminRepository fakeAdminRepository;

    @BeforeEach
    void setUp() {
        fakeAdminRepository = new FakeAdminRepository();
        adminQueryService = new AdminQueryService(fakeAdminRepository);
    }

    @Test
    @DisplayName("이메일이 중복될 경우 예외가 발생한다.")
    void 이메일_중복_검증_테스트() {
        // given
        fakeAdminRepository.save(Admin.create("super@goorm.com", "1234", Role.SUPER));

        // when & then
        assertThatThrownBy(() -> adminQueryService.validateEmailUniqueness("super@goorm.com"))
                .isInstanceOf(AdminEmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("존재하지 않는 관리자를 조회하면 예외가 발생한다.")
    void 존재하지_않는_관리자_조회_테스트() {
        assertThatThrownBy(() -> adminQueryService.getById(999L))
                .isInstanceOf(AdminNotFoundException.class);
    }

    @Test
    @DisplayName("관리자를 페이지네이션하여 조회할 수 있다.")
    void 관리자_페이지네이션_조회_테스트() {
        // given
        for (int i = 0; i < 10; i++) {
            fakeAdminRepository.save(Admin.create("super" + i + "@goorm.com", "1234", Role.SUPER));
        }

        // when
        Page<Admin> admins = adminQueryService.getAdminsByPage(PageRequest.of(0, 10));

        // then
        assertThat(admins.getTotalElements()).isEqualTo(10);
        assertThat(admins.getContent().size()).isEqualTo(10);
    }


    @Test
    @DisplayName("이메일을 기반으로 관리자를 조회할 수 있다.")
    void 이메일_기반_관리자_조회_테스트() {
        // given
        fakeAdminRepository.save(Admin.create("super@goorm.com", "1234", Role.SUPER));

        // when
        Admin foundAdmin = adminQueryService.getAdminByEmail("super@goorm.com");

        // then
        assertThat(foundAdmin).isNotNull();
        assertThat(foundAdmin.getEmail()).isEqualTo("super@goorm.com");
    }

    @Test
    @DisplayName("ID를 기반으로 관리자를 조회할 수 있다.")
    void ID_기반_관리자_조회_테스트() {
        // given
        Admin admin = fakeAdminRepository.save(Admin.create("super@goorm.com", "1234", Role.SUPER));
        Long adminId = admin.getId();

        // when
        Admin foundAdmin = adminQueryService.getById(adminId);

        // then
        assertThat(foundAdmin).isNotNull();
        assertThat(foundAdmin.getId()).isEqualTo(adminId);
    }

    @Test
    @DisplayName("저장된 관리자가 존재하는지 확인할 수 있다.")
    void 저장된_관리자_존재_테스트() {
        // given
        Admin admin = fakeAdminRepository.save(Admin.create("super@goorm.com", "1234", Role.SUPER));

        // when
        Optional<Admin> foundAdmin = fakeAdminRepository.findById(admin.getId());

        // then
        assertThat(foundAdmin).isPresent();
    }
}

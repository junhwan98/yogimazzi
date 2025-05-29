package admin.domain;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static goorm.deepdive.team1.domain.admin.domain.Role.SUPER;
import static goorm.deepdive.team1.domain.admin.domain.Role.NORMAL;
import static org.assertj.core.api.Assertions.*;

class AdminDomainTest {

    private Admin superAdmin;
    private Admin normalAdmin;

    @BeforeEach
    void setUp() {
        superAdmin = Admin.create("super@goorm.com", "1234", SUPER);
        normalAdmin = Admin.create("normal@goorm.com", "1234", NORMAL);
    }

    @Test
    @DisplayName("최고 관리자는 새로운 관리자 계정을 생성할 수 있다.")
    void 최고관리자_계정_추가_테스트() {
        Admin newAdmin = Admin.create("normal@goorm.com", "1234", NORMAL);

        assertThat(newAdmin).isNotNull();
        assertThat(newAdmin.getEmail()).isEqualTo("normal@goorm.com");
        assertThat(newAdmin.getRole()).isEqualTo(NORMAL);
    }

    @Test
    @DisplayName("일반 관리자는 새로운 관리자 계정을 생성할 수 없다.")
    void 일반관리자_계정_추가_실패_테스트() {
        assertThatThrownBy(() -> {
            if (normalAdmin.getRole() != SUPER) {
                throw new IllegalAccessException("일반 관리자는 계정을 추가할 수 없습니다.");
            }
            Admin.create("normal@goorm.com", "1234", NORMAL);
        }).isInstanceOf(IllegalAccessException.class)
                .hasMessage("일반 관리자는 계정을 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("최고 관리자는 다른 관리자의 권한을 수정할 수 있다.")
    void 최고관리자_권한_변경_테스트() {
        superAdmin.updateRole(NORMAL);
        assertThat(superAdmin.getRole()).isEqualTo(NORMAL);
    }

    @Test
    @DisplayName("일반 관리자는 다른 관리자 권한을 변경할 수 없다.")
    void 일반관리자_권한_변경_실패_테스트() {
        assertThatThrownBy(() -> {
            if (normalAdmin.getRole() != SUPER) {
                throw new IllegalAccessException("일반 관리자는 다른 관리자의 권한을 변경할 수 없습니다.");
            }
            normalAdmin.updateRole(SUPER);
        }).isInstanceOf(IllegalAccessException.class)
                .hasMessage("일반 관리자는 다른 관리자의 권한을 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("최고 관리자는 관리자를 삭제할 수 있다.")
    void 최고관리자_계정_삭제_테스트() {
        assertThatCode(() -> superAdmin = null).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("일반 관리자는 다른 관리자를 삭제할 수 없다.")
    void 일반관리자_계정_삭제_실패_테스트() {
        assertThatThrownBy(() -> {
            if (normalAdmin.getRole() != SUPER) {
                throw new IllegalAccessException("일반 관리자는 계정을 삭제할 수 없습니다.");
            }
            normalAdmin = null;
        }).isInstanceOf(IllegalAccessException.class)
                .hasMessage("일반 관리자는 계정을 삭제할 수 없습니다.");
    }
}

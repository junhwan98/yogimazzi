package goorm.deepdive.team1.api.admin.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.api.admin.presentation.request.AdminPasswordUpdateRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminRegisterRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminRoleUpdateRequest;
import goorm.deepdive.team1.api.admin.presentation.response.AdminRegisterResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminReissueResponse;
import goorm.deepdive.team1.api.jwt.CookieUtil;
import goorm.deepdive.team1.api.jwt.JwtUtil;
import goorm.deepdive.team1.api.jwt.exception.JwtEmptyException;
import goorm.deepdive.team1.api.jwt.exception.JwtExpiredException;
import goorm.deepdive.team1.api.jwt.exception.JwtRedisStorageException;
import goorm.deepdive.team1.domain.admin.application.AdminCommandService;
import goorm.deepdive.team1.domain.admin.application.AdminQueryService;
import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.infrastructure.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminFacade {
    private final AdminCommandService adminCommandService;
    private final AdminQueryService adminQueryService;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public AdminRegisterResponse register(AdminRegisterRequest request) {
        adminQueryService.validateEmailUniqueness(request.email());

        Admin admin = adminCommandService.register(request.email(), request.password(), request.role());
        return AdminRegisterResponse.from(admin);
    }

    public AdminReissueResponse reissueToken(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 리프레시 토큰 가져오기
        String refreshToken = CookieUtil.getRefreshToken(request);
        if (refreshToken == null) {
            throw new JwtEmptyException();
        }

        // Redis에서 저장된 리프레시 토큰 확인
        String adminId = jwtUtil.getRefreshTokenAdminId(refreshToken);
        String storedRefreshToken = tokenRepository.getRefreshToken(adminId);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new JwtRedisStorageException();
        }

        // 리프레시 토큰 유효성 검증
        if (jwtUtil.isRefreshTokenExpired(refreshToken)) {
            throw new JwtExpiredException();
        }

        // 레디스에 저장된 토큰 검증이 통과한 경우 정상 요청으로 판단, 새 토큰 생성
        String newAccessToken = "Bearer " +jwtUtil.createAccessToken(
                adminId,
                jwtUtil.getRefreshTokenEmail(refreshToken),
                jwtUtil.getRefreshTokenRole(refreshToken)
        );
        String newRefreshToken = jwtUtil.createRefreshToken(
                adminId,
                jwtUtil.getRefreshTokenEmail(refreshToken),
                jwtUtil.getRefreshTokenRole(refreshToken)
        );

        // 기존 리프레시 토큰 삭제 & 새로운 리프레시 토큰 저장
        tokenRepository.deleteRefreshToken(adminId);
        tokenRepository.saveRefreshToken(adminId, newRefreshToken, 60 * 60 * 24 * 7);

        // 응답 설정
        response.setHeader("Authorization", newAccessToken);
        response.addCookie(CookieUtil.createCookie("Refresh-Token", newRefreshToken, 604800));

        return new AdminReissueResponse(Long.parseLong(adminId), newAccessToken);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {

        // 사용자가 로그아웃 요청시 Access,refresh를 제거한 상태에서 로그아웃 요청이 가능한가에 대한 고민 필요함
        String refreshToken = CookieUtil.getRefreshToken(request);
        if (refreshToken == null) {
            throw new JwtEmptyException();
        }

        // Redis에서 저장된 리프레시 토큰 삭제
        String adminId = jwtUtil.getRefreshTokenAdminId(refreshToken);
        tokenRepository.deleteRefreshToken(adminId);

        // 응답에서 토큰 삭제 (쿠키 및 헤더 초기화)
        response.setHeader("Authorization", ""); // 헤더 제거
        CookieUtil.clearAuthCookie(response, "Refresh-Token"); // 쿠키 삭제

    }

    @Transactional
    public void deleteAdmin(Long id) {
        Admin admin = adminQueryService.getById(id);
        adminCommandService.deleteAdmin(admin);
    }

    @Transactional
    public void updatePassword(Long id, AdminPasswordUpdateRequest request) {
        Admin admin = adminQueryService.getById(id);
        adminCommandService.updatePassword(admin, request.oldPassword(), request.newPassword());
    }

    public Page<Admin> getAdminsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        return adminQueryService.getAdminsByPage(pageable);
    }

    public Admin getAdminByEmail(String email) {
        return adminQueryService.getAdminByEmail(email);
    }

    @Transactional
    public void updateAdminRole(Long adminId, AdminRoleUpdateRequest request, Long loggedInAdminId) {
        Admin admin = adminQueryService.getById(adminId);
        adminCommandService.updateAdminRole(admin, request.role(), loggedInAdminId);
    }

}

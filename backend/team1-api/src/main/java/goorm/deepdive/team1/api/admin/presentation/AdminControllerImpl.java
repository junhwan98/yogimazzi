package goorm.deepdive.team1.api.admin.presentation;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import goorm.deepdive.team1.api.admin.application.AdminFacade;
import goorm.deepdive.team1.api.admin.presentation.request.AdminLoginRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminPasswordUpdateRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminRegisterRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminRoleUpdateRequest;
import goorm.deepdive.team1.api.admin.presentation.response.AdminListResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminRegisterResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminReissueResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminSearchResponse;
import goorm.deepdive.team1.api.security.CustomAdminDetails;
import goorm.deepdive.team1.domain.admin.domain.Admin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController{
    private final AdminFacade adminFacade;

    @Override
    @PostMapping("/register")
    public ResponseEntity<AdminRegisterResponse> register(@RequestBody AdminRegisterRequest request) {
        AdminRegisterResponse response = adminFacade.register(request);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AdminLoginRequest request) {
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<AdminReissueResponse> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        AdminReissueResponse registerResponse = adminFacade.reissueToken(request, response);
        return ResponseEntity.ok(registerResponse);
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        adminFacade.logout(request, response);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long adminId) {
        adminFacade.deleteAdmin(adminId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{adminId}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long adminId,
            @RequestBody AdminPasswordUpdateRequest request) {
        adminFacade.updatePassword(adminId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<Page<AdminListResponse>> getAdminsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AdminListResponse> response = adminFacade.getAdminsByPage(page, size)
                .map(AdminListResponse::from);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<AdminSearchResponse> getAdminByEmail(@RequestParam String email) {
        Admin admin = adminFacade.getAdminByEmail(email);
        AdminSearchResponse response = AdminSearchResponse.from(admin);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{adminId}/role")
    @PreAuthorize("hasAuthority('SUPER')")
    public ResponseEntity<Void> updateAdminRole(
            @PathVariable Long adminId,
            @RequestBody AdminRoleUpdateRequest request,
            @AuthenticationPrincipal CustomAdminDetails adminDetails
    ) {
        adminFacade.updateAdminRole(adminId, request, adminDetails.getAdminId());
        return ResponseEntity.ok().build();
    }
}

package goorm.deepdive.team1.api.admin.presentation;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import goorm.deepdive.team1.api.admin.presentation.request.AdminLoginRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminPasswordUpdateRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminRegisterRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminRoleUpdateRequest;
import goorm.deepdive.team1.api.admin.presentation.response.AdminListResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminLoginResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminRegisterResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminReissueResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminSearchResponse;
import goorm.deepdive.team1.api.security.CustomAdminDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Tag(name = "ADMIN", description = "관리자 API")  // Swagger 태그 추가
public interface AdminController {

    @Operation(summary = "관리자 회원가입 API", description = """
            - Description : 이 API는 새로운 관리자를 등록할 수 있습니다.
            - role은 2종류가 있습니다 : SUPER("최고 관리자"),NORMAL("일반 관리자"),
        """)
    @ApiResponse(
            responseCode = "201",
            description = "회원가입 성공",
            content = @Content(schema = @Schema(implementation = AdminRegisterResponse.class))
    )
    @PostMapping("/register")
    ResponseEntity<AdminRegisterResponse> register(@Valid @RequestBody AdminRegisterRequest request);

    @Operation(summary = "관리자 로그인 API", description = """
        - Description : 이 API를 통해 관리자가 로그인할 수 있습니다.
        - 요청 성공 시 JWT 액세스 토큰과 리프레시 토큰을 반환합니다.
    """)
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(schema = @Schema(implementation = AdminLoginResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "로그인 실패 - 잘못된 자격 증명")
    @PostMapping("/login")
    ResponseEntity<Void> login(@Valid @RequestBody AdminLoginRequest request);

    @Operation(summary = "JWT 재발급 API", description = """
            - Description : 액세스 토큰이 만료되었을 때, 새로운 토큰을 발급받을 수 있습니다.
        """)
    @ApiResponse(
            responseCode = "200",
            description = "토큰 재발급 성공",
            content = @Content(schema = @Schema(implementation = AdminReissueResponse.class))
    )
    @PostMapping("/reissue")
    ResponseEntity<AdminReissueResponse> reissueToken(HttpServletRequest request, HttpServletResponse response);

    @Operation(summary = "관리자 로그아웃 API", description = """
            - Description : 로그아웃을 수행하며, 리프레시 토큰을 삭제하고 세션을 종료합니다.
        """)
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @PostMapping("/logout")
    ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response);

    @Operation(summary = "관리자 삭제 API", description = """
            - Description : 특정 관리자를 삭제하는 API입니다.
        """)
    @ApiResponse(responseCode = "204", description = "관리자 삭제 성공")
    @ApiResponse(responseCode = "404", description = "관리자를 찾을 수 없음")
    @DeleteMapping("/{adminId}")
    ResponseEntity<Void> deleteAdmin(@PathVariable Long adminId);

    @Operation(summary = "관리자 비밀번호 변경 API", description = """
            - Description : 기존 비밀번호를 검증한 후 새 비밀번호로 변경하는 API입니다.
        """)
    @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공")
    @ApiResponse(responseCode = "401", description = "기존 비밀번호 불일치")
    @ApiResponse(responseCode = "404", description = "관리자를 찾을 수 없음")
    @PatchMapping("/{adminId}/password")
    ResponseEntity<Void> updatePassword(
            @PathVariable Long adminId,
            @RequestBody AdminPasswordUpdateRequest request);

    @Operation(summary = "페이징된 관리자 목록 조회 API", description = """
        - Description : 등록된 관리자를 페이지 단위로 조회하는 API입니다.
        - `page`: 0부터 시작하는 페이지 번호
        - `size`: 한 페이지당 조회할 개수
    """)
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/list")
    ResponseEntity<Page<AdminListResponse>> getAdminsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "관리자 이메일 검색 API", description = """
        - Description : 특정 이메일을 가진 관리자를 조회하는 API입니다.
        - 요청 파라미터로 `email` 값을 전달하면 해당 이메일을 가진 관리자를 반환합니다.
    """)
    @ApiResponse(responseCode = "200", description = "관리자 조회 성공", content = @Content(schema = @Schema(implementation = AdminSearchResponse.class)))
    @ApiResponse(responseCode = "404", description = "관리자를 찾을 수 없음")
    @GetMapping("/search")
    ResponseEntity<AdminSearchResponse> getAdminByEmail(@RequestParam String email);

    @Operation(summary = "관리자 권한 변경 API", description = """
        - Description : 특정 관리자의 권한을 변경하는 API입니다.
        - 요청 파라미터로 `adminId`와 `새로운 Role` 값을 전달하면 해당 관리자의 권한이 업데이트됩니다.
    """)
    @ApiResponse(responseCode = "200", description = "권한 변경 성공")
    @ApiResponse(responseCode = "403", description = "사용자 자신의 권한을 변경하려는 경우 403에러")
    @ApiResponse(responseCode = "403", description = "role이 SUPER가 아닌 계정이 권한 변경하는 경우 403에러")
    @ApiResponse(responseCode = "404", description = "관리자를 찾을 수 없음")
    @PatchMapping("/{adminId}/role")
    ResponseEntity<Void> updateAdminRole(
            @PathVariable Long adminId,
            @RequestBody AdminRoleUpdateRequest request,
            @AuthenticationPrincipal CustomAdminDetails adminDetails);
}
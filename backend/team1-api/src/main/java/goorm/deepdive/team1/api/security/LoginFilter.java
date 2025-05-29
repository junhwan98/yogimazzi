package goorm.deepdive.team1.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.deepdive.team1.api.jwt.CookieUtil;
import goorm.deepdive.team1.api.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/admin/login"); // 인증 URL 설정
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), Map.class);
            String email = credentials.get("email");
            String password = credentials.get("password");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            return authenticationManager.authenticate(authToken); //authenticate() 호츌시 loadUserByUsername 실행됨
        } catch (IOException e) {
            throw new RuntimeException("로그인 요청 파싱 오류", e); // 이 부분은 파싱 오류일 때만 예외 처리
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomAdminDetails adminDetails = (CustomAdminDetails) authentication.getPrincipal();
        String email = adminDetails.getUsername(); // enail을 getUsername로 한 이유는 getUsername이 정해진 메서드명입니다
        Long adminId = adminDetails.getAdminId();
        String role = adminDetails.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority())
                .orElse("ROLE_USER");

        // JWT 생성
        String token = jwtUtil.createAccessToken(String.valueOf(adminId), email, role); // 1시간 유효기간
        String refreshToken = jwtUtil.createRefreshToken(String.valueOf(adminId),email, role);
        response.addHeader("Authorization", "Bearer " + token);
        response.addCookie(CookieUtil.createCookie("Refresh-Token", refreshToken, 604800));
//        response.addHeader("Refresh-Token", refreshToken);
        // JSON 응답 생성
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, String> AdminResponse = new HashMap<>();
            AdminResponse.put("message", "Login successful");
            AdminResponse.put("id", String.valueOf(adminId));
            AdminResponse.put("email", email);
            AdminResponse.put("Accesstoken","Bearer " + token);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getOutputStream(), AdminResponse);
        } catch (Exception e) {
            throw new RuntimeException("응답 생성 중 오류 발생", e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401 에러
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
        errorResponse.put("message", "ID 또는 비밀번호가 일치하지 않습니다.");
        errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        ObjectMapper mapper = new ObjectMapper();
        try (PrintWriter writer = response.getWriter()) {
            writer.write(mapper.writeValueAsString(errorResponse));
            writer.flush();
            response.flushBuffer();
        }
    }
}




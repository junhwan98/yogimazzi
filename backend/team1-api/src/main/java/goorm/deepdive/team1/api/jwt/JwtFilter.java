package goorm.deepdive.team1.api.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.deepdive.team1.common.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_HEADER = "Authorization";

    private final JwtUtil jwtUtil;
//    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 요청에서 액세스 토큰 가져오기
            String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
            String token = extractToken(accessToken);

            // 토큰 검증 및 인증 객체 생성
            if (jwtUtil.validateToken(token)) {
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            setErrorResponse(e, response);
        }
    }

    // 헤더 토큰 추출
    private String extractToken(String accessToken) {
        if (accessToken != null && accessToken.startsWith(BEARER_PREFIX)) {
            return accessToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private void setErrorResponse(CustomException exception, HttpServletResponse response) throws IOException {
        // 확장성과 json 직렬화로 코드 가독성을 위해 ObjectMapper를 사용해 JSON 응답 생성
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> errorResponse = Map.of(
                "status", exception.getCode().getStatus().value(), // HTTP 상태 코드
                "error", exception.getCode().getCode(), // 예외 코드
                "message", exception.getCode().getMessage() // 예외 메시지
        );

        response.setStatus(exception.getCode().getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 변환 후 응답
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }


//    해당 JWT 필터 자체가 실행되지 않도록 설정하는 코드지만  permitAll() 대처하는 방안으로 리뷰 받음
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        return uri.contains("login") || uri.contains("swagger-ui") || uri.contains("swagger-resources")
                || uri.contains("v3/api-docs") || uri.contains("webjars")
                || uri.contains("/reissue")
                || uri.contains("/register") // 회원가입 엔드포인트 추가
                || uri.contains("/logout")
                || uri.contains("/actuator");
    }


//    블랙리스트(로그아웃된) 토큰인지 확인
//    private boolean isTokenBlacklisted(String token) {
//        return Boolean.TRUE.equals(redisTemplate.hasKey("admin:blacklist:refreshToken:" + token));
//    }

}
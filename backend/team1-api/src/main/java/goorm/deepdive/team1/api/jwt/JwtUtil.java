package goorm.deepdive.team1.api.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import goorm.deepdive.team1.api.jwt.exception.JwtExpiredException;
import goorm.deepdive.team1.api.jwt.exception.JwtInvalidException;
import goorm.deepdive.team1.api.jwt.exception.JwtMalformedException;
import goorm.deepdive.team1.api.jwt.exception.JwtSignatureInvalidException;
import goorm.deepdive.team1.api.jwt.exception.JwtUnsupportedException;
import goorm.deepdive.team1.api.security.CustomAdminDetails;
import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.domain.Role;
import goorm.deepdive.team1.domain.admin.infrastructure.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

// 토큰 생성과 검증 로직을 담은 클래스
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final TokenRepository tokenRepository;
    private final JwtProperties jwtProperties;

    private SecretKey Access_secretKey;
    private SecretKey refrsh_secretKey;
    
    @PostConstruct
    public void init() {
        this.Access_secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        this.refrsh_secretKey = Keys.hmacShaKeyFor(jwtProperties.getRefreshSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String adminId,String email ,String role) {
        long expirationMs = jwtProperties.getExpirationSeconds() * 1000000000; // 만료시간 (ex expirationSeconds가 600인경우 => 10분)
        return Jwts.builder()
                .claim("AdminId", adminId)
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(Access_secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String adminId,String email, String role) {
        long expirationMs = jwtProperties.getRefreshExpirationSeconds() * 1000; // 7일
        String refreshToken = Jwts.builder()
                .claim("AdminId", adminId)
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(refrsh_secretKey, SignatureAlgorithm.HS256)
                .compact();

        // 기존 리프레시 토큰이 존재하는지 확인
        String existingRefreshToken = tokenRepository.getRefreshToken(adminId);

        if (existingRefreshToken != null) {
            tokenRepository.deleteRefreshToken(adminId); // 기존 리프레시 토큰 삭제
        }
        // 새로운 리프레시 토큰 생성 후 저장
        tokenRepository.saveRefreshToken(adminId, refreshToken, jwtProperties.getRefreshExpirationSeconds());

        return refreshToken;
    }

    public String getAdminId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Access_secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("AdminId", String.class);
    }

    public String getRefreshTokenAdminId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refrsh_secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("AdminId", String.class);
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Access_secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    public String getRefreshTokenEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refrsh_secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    public String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Access_secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public String getRefreshTokenRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refrsh_secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // Refresh AccessToken이 만료되었는지 확인 */
    public Boolean isExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Access_secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // Refresh Token이 만료되었는지 확인 */
    public Boolean isRefreshTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refrsh_secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String adminId = claims.get("AdminId", String.class);
        String role = claims.get("role", String.class);

        Admin adminEntity = Admin.builder()
                .id(Long.valueOf(adminId))
                .role(Role.valueOf(role))
                .build();

        CustomAdminDetails adminDetails = new CustomAdminDetails(adminEntity);
        return new UsernamePasswordAuthenticationToken(adminDetails, null, adminDetails.getAuthorities());
    }

    private Claims getClaims(String token) {
        // 토큰을 디코딩(파싱)해서 Claims 객체로 변환
        return Jwts.parserBuilder()
                .setSigningKey(Access_secretKey)  // Access Token 서명 키 사용
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Access_secretKey)
                    .build()
                    .parseClaimsJws(token);

            return true; // 유효한 토큰
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException(); // 만료된 토큰 예외 던지기
        } catch (UnsupportedJwtException e) {
            throw new JwtUnsupportedException(); // 지원되지 않는 형식
        } catch (MalformedJwtException e) {
            throw new JwtMalformedException(); // 잘못된 형식의 토큰
        } catch (SignatureException e) {
            throw new JwtSignatureInvalidException(); // 서명 오류
        } catch (IllegalArgumentException e) {
            throw new JwtInvalidException(); // 빈 토큰 또는 잘못된 값
        }
    }




    public void saveRefreshToken(String email, String refreshToken) {
        // Redis 또는 DB에 Refresh Token을 저장하는 로직 추가 가능
    }
}
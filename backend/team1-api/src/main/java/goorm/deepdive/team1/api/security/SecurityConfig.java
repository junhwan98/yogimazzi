package goorm.deepdive.team1.api.security;

import java.util.Arrays;
import java.util.Collections;

import goorm.deepdive.team1.api.jwt.JwtUtil;
import goorm.deepdive.team1.common.exception.CustomAccessDeniedHandler;
import goorm.deepdive.team1.api.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import goorm.deepdive.team1.infra.config.base.Team1Config;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig implements Team1Config {

    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,  AuthenticationManager authenticationManager) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager, jwtUtil);

        return http
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_PATTERNS).permitAll()
                        .requestMatchers(STATIC_RESOURCES_PATTERNS).permitAll()
                        .requestMatchers(PERMIT_ALL_PATTERNS).permitAll()
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().permitAll()
                )
//				ex) 권한 제한 예시 .requestMatchers("/api/users/**").hasAuthority("SUPER")
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler) // AccessDeniedHandler 등록
                )
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
                .build();
    }
    private static final String[] SWAGGER_PATTERNS = {
            "/swagger-ui/**",
            "/actuator/**",
            "/v3/api-docs/**",
    };

    private static final String[] STATIC_RESOURCES_PATTERNS = {
            "/img/**",
            "/css/**",
            "/js/**"
    };

    private static final String[] PERMIT_ALL_PATTERNS = {
            "/error",
            "/favicon.ico",
            "/index.html",
            "/",
    };

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/users/**",
            "/api/admin/**"

    };


    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","PATCH"));
            config.setAllowedOriginPatterns(Arrays.asList("*"));
            config.setAllowCredentials(true);
            return config;
        };
    }

//	@Bean
//	public AuthenticationManager authenticationManager(
//		BCryptPasswordEncoder bCryptPasswordEncoder
//	) {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//		authProvider.setPasswordEncoder(bCryptPasswordEncoder);
//		return new ProviderManager(authProvider);
//	}


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


package com.recommand.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable()) // 최신 방식으로 CSRF 비활성화
        .authorizeHttpRequests(auth -> auth
            //.requestMatchers("/api/**").permitAll() // 회원가입 허용
            //.requestMatchers("/email/**").permitAll() // 이메일을 통한 인증 허용
            //.requestMatchers("/uploads/images/**").permitAll()
            .requestMatchers(
                    "/",                 // 루트 URL
                    "/index.html",       // React 진입점
                    "/static/**",        // js/css
                    "/js/**", "/css/**", "/images/**", // 혹시 모를 폴더
                    "/favicon.ico",
                    "/api/**",           // 기존 설정 유지
                    "/email/**",
                    "/uploads/images/**"
                ).permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form.disable()); // 기본 로그인 비활성화

        return http.build();
    }
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
//    @PostConstruct
//    public void init() {
//        System.out.println("SecurityConfig 적용됨!");
//    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("사용자 없음");
        };
    }
}

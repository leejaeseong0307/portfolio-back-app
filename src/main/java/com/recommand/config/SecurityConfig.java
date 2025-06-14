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
                    "/", 
                    "/index.html",
                    "/static/**",
                    "/js/**", "/css/**", "/images/**",
                    "/favicon.ico",
                    "/logo192.png", "/manifest.json",
                    "/uploads/images/**",
                    "/api/**",
                    "/api/session",
                    "/email/**"
                    ,"/home", "/long", "/longView/**", "/longForm/**", 
                    "/short", "/profile", "/my", "/register", "/login", 
                    "/findId", "/findPw", "/terms", "/privacy"//, "/sitemap.xml", "/robots.txt"
                    //"/**"
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

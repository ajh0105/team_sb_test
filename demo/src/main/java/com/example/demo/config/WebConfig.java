package com.example.demo.config;

import com.example.demo.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1) // 인터셉터 실행 순서
                .addPathPatterns("/**") // 모든 경로를 일단 감시하되
                .excludePathPatterns(
                        "/", "/index", "/login", "/signup", "/logout",
                        "/check-email",     // 이메일 중복 체크 Ajax
                        "/sub_*",           // MemberController에서 처리하는 서브페이지들 제외
                        "/css/**", "/js/**", "/images/**", "/icon/**",
                        "/fonts/**",        // 폰트 정적 리소스
                        "/error", "/favicon.ico"
                );
    }
}
package com.example.demo.config;

import com.example.demo.interceptor.AdminCheckInterceptor;
import com.example.demo.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 로그인 체크 인터셉터
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/index", "/login", "/signup", "/logout",
                        "/check-email",
                        "/sub_*",
                        "/community/free", "/community/qna", "/community/report",
                        "/community/{id:[0-9]+}",
                        "/css/**", "/js/**", "/images/**", "/icon/**",
                        "/fonts/**",
                        "/error", "/favicon.ico"
                );

        // 관리자 전용 인터셉터
        registry.addInterceptor(new AdminCheckInterceptor())
                .order(2)
                .addPathPatterns("/admin/**");
    }
}
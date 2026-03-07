package com.example.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        // 세션이 없으면 새로 생성하지 않도록 false 설정
        HttpSession session = request.getSession(false);

        // 1. 세션이 아예 없거나, "loginMember"라는 이름의 데이터가 없는 경우
        if (session == null || session.getAttribute("loginMember") == null) {
            log.info("미인증 사용자 요청 거부: {}", requestURI);

            // 2. 로그인 페이지로 강제 이동 (가려던 주소를 쿼리 파라미터로 저장)
            // WebConfig에서 열어준 "/login" 경로를 사용합니다.
            response.sendRedirect("/login?redirectURL=" + requestURI);

            return false; // 더 이상 진행하지 않고 차단
        }

        // 3. 로그인된 상태라면 컨트롤러로 정상 진행
        return true;
    }
}
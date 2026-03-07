package com.example.demo.interceptor;

import com.example.demo.entity.Member;
import com.example.demo.entity.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("/login");
            return false;
        }

        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null || loginMember.getRole() != Role.ADMIN) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}

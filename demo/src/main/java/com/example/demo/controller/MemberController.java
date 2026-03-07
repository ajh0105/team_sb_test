package com.example.demo.controller;

import com.example.demo.entity.Member;
import com.example.demo.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // --- 메인 페이지 (templates 바로 아래) ---
    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    // --- 41개 서브페이지 통합 처리 (templates/layout/ 아래) ---
    @GetMapping("/{pageName}")
    public String dynamicSubPage(@PathVariable String pageName) {
        if (pageName.startsWith("sub_")) {
            // layout 폴더 안의 파일을 호출하도록 수정
            return "layout/" + pageName;
        }
        return "index";
    }

    // --- 로그인 페이지 이동 (templates/board/ 아래) ---
    @GetMapping("/login")
    public String loginForm() {
        return "board/login";
    }

    // --- 회원가입 페이지 이동 (templates/board/ 아래) ---
    @GetMapping("/signup")
    public String signupForm() {
        return "board/signup";
    }

    // --- 로그인 처리 ---
    @PostMapping("/login")
    public String login(@RequestParam("username") String email,
                        @RequestParam("password") String password,
                        HttpSession session) {
        try {
            Member loginMember = memberService.login(email, password);
            session.setAttribute("loginMember", loginMember);
            return "redirect:/index";
        } catch (IllegalArgumentException e) {
            return "redirect:/login?error";
        }
    }

    // --- 회원가입 처리 ---
    @PostMapping("/signup")
    public String signup(@RequestParam("name") String name,
                         @RequestParam("email") String email,
                         @RequestParam("password") String password,
                         @RequestParam("phone") String phone) {
        try {
            memberService.join(name, email, password, phone);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            return "redirect:/signup?error";
        }
    }

    // --- 로그아웃 처리 ---
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }
}
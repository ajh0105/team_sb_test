package com.example.demo.controller;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.MemberFormDto;
import com.example.demo.entity.Member;
import com.example.demo.entity.Role;
import com.example.demo.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        // 커뮤니티 페이지는 BoardController로 리다이렉트
        if ("sub_commu_1".equals(pageName)) return "redirect:/community/free";
        if ("sub_commu_2".equals(pageName)) return "redirect:/community/qna";
        if ("sub_commu_3".equals(pageName)) return "redirect:/community/report";
        if (pageName.startsWith("sub_")) {
            return "layout/" + pageName;
        }
        return "index";
    }

    // --- 로그인 페이지 ---
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "board/login";
    }

    // --- 로그인 처리 ---
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginDto") LoginDto dto,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model) {
        if (bindingResult.hasErrors()) {
            return "board/login";
        }

        try {
            Member loginMember = memberService.login(dto);
            session.setAttribute("loginMember", loginMember);
            if (loginMember.getRole() == Role.ADMIN) {
                return "redirect:/admin";
            }
            return "redirect:/index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "board/login";
        }
    }

    // --- 회원가입 페이지 ---
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "board/signup";
    }

    // --- 회원가입 처리 ---
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("memberFormDto") MemberFormDto dto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return "board/signup";
        }

        try {
            memberService.join(dto);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "board/signup";
        } catch (Exception e) {
            model.addAttribute("errorMsg", "회원가입 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return "board/signup";
        }
    }

    // --- 이메일 중복 체크 (Ajax) ---
    @GetMapping("/check-email")
    @ResponseBody
    public boolean checkEmail(@RequestParam String email) {
        return !memberService.existsByEmail(email);
    }

    // --- 로그아웃 처리 ---
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }
}

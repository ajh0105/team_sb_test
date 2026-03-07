package com.example.demo.controller;

import com.example.demo.repository.MemberRepository;
import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberRepository memberRepository;
    private final BoardService boardService;

    @GetMapping({"", "/"})
    public String dashboard(Model model) {
        model.addAttribute("memberCount", memberRepository.count());
        model.addAttribute("boardCount", boardService.findAll().size());
        return "admin/index";
    }

    @GetMapping("/members")
    public String memberList(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "admin/members";
    }

    @GetMapping("/boards")
    public String boardList(Model model) {
        model.addAttribute("boards", boardService.findAll());
        return "admin/boards";
    }

    @PostMapping("/boards/{id}/delete")
    public String deleteBoard(@PathVariable Long id) {
        boardService.deleteByAdmin(id);
        return "redirect:/admin/boards";
    }

    @PostMapping("/boards/{id}/qna-status")
    public String updateQnaStatus(@PathVariable Long id,
                                   @RequestParam String status) {
        boardService.updateQnaStatus(id, status);
        return "redirect:/admin/boards";
    }

    @PostMapping("/boards/{id}/report-step")
    public String updateReportStep(@PathVariable Long id,
                                    @RequestParam String step) {
        boardService.updateReportStep(id, step);
        return "redirect:/admin/boards";
    }
}

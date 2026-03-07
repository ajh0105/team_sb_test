package com.example.demo.controller;

import com.example.demo.dto.BoardFormDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.BoardType;
import com.example.demo.entity.Member;
import com.example.demo.service.BoardService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // --- 자유게시판 목록 ---
    @GetMapping("/free")
    public String freeList(Model model) {
        model.addAttribute("boards", boardService.findByType(BoardType.FREE));
        model.addAttribute("boardType", "FREE");
        model.addAttribute("boardTypeName", "자유게시판");
        return "community/free";
    }

    // --- Q&A 목록 ---
    @GetMapping("/qna")
    public String qnaList(Model model) {
        model.addAttribute("boards", boardService.findByType(BoardType.QNA));
        model.addAttribute("boardType", "QNA");
        model.addAttribute("boardTypeName", "Q&A");
        return "community/qna";
    }

    // --- 관광불편신고 목록 ---
    @GetMapping("/report")
    public String reportList(Model model) {
        model.addAttribute("boards", boardService.findByType(BoardType.REPORT));
        model.addAttribute("boardType", "REPORT");
        model.addAttribute("boardTypeName", "관광불편신고");
        return "community/report";
    }

    // --- 글쓰기 폼 ---
    @GetMapping("/write")
    public String writeForm(@RequestParam(defaultValue = "FREE") String type,
                            HttpSession session, Model model) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }
        model.addAttribute("boardFormDto", new BoardFormDto());
        model.addAttribute("boardType", type);
        return "community/form";
    }

    // --- 글쓰기 처리 ---
    @PostMapping("/write")
    public String write(@Valid @ModelAttribute("boardFormDto") BoardFormDto dto,
                        BindingResult bindingResult,
                        HttpSession session, Model model) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("boardType", dto.getBoardType());
            return "community/form";
        }

        try {
            boardService.save(dto, loginMember);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "게시글 저장 중 오류가 발생했습니다.");
            return "community/form";
        }

        return switch (dto.getBoardType()) {
            case "QNA" -> "redirect:/community/qna";
            case "REPORT" -> "redirect:/community/report";
            default -> "redirect:/community/free";
        };
    }

    // --- 상세 보기 ---
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Board board = boardService.increaseViews(id);
        model.addAttribute("board", board);
        return "community/detail";
    }

    // --- 삭제 ---
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }

        Board board = boardService.findById(id);
        BoardType type = board.getBoardType();

        boardService.delete(id, loginMember);

        return switch (type) {
            case QNA -> "redirect:/community/qna";
            case REPORT -> "redirect:/community/report";
            default -> "redirect:/community/free";
        };
    }
}

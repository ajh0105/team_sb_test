package com.example.demo.service;

import com.example.demo.dto.BoardFormDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.BoardType;
import com.example.demo.entity.Member;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> findByType(BoardType boardType) {
        return boardRepository.findByBoardTypeOrderByIdDesc(boardType);
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    public List<Board> findAll() {
        return boardRepository.findAllByOrderByIdDesc();
    }

    @Transactional
    public Long save(BoardFormDto dto, Member member) {
        BoardType boardType = BoardType.valueOf(dto.getBoardType());

        Board board = Board.builder()
                .boardType(boardType)
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(member)
                .secret(dto.isSecret())
                .build();

        // 기본 상태 설정
        if (boardType == BoardType.QNA) {
            board.setQnaStatus("답변대기");
        } else if (boardType == BoardType.REPORT) {
            board.setReportStep("접수");
            board.setSecret(true); // 신고 게시판은 기본 비밀글
        }

        Board saved = boardRepository.save(board);
        log.info("게시글 저장 완료: ID={}, Type={}", saved.getId(), saved.getBoardType());
        return saved.getId();
    }

    @Transactional
    public Board increaseViews(Long id) {
        Board board = findById(id);
        board.increaseViews();
        return board;
    }

    @Transactional
    public void delete(Long id, Member loginMember) {
        Board board = findById(id);
        if (!board.getMember().getId().equals(loginMember.getId())
                && loginMember.getRole().name().equals("USER")) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        boardRepository.delete(board);
        log.info("게시글 삭제: ID={}", id);
    }

    @Transactional
    public void deleteByAdmin(Long id) {
        Board board = findById(id);
        boardRepository.delete(board);
        log.info("관리자 게시글 삭제: ID={}", id);
    }

    @Transactional
    public void updateQnaStatus(Long id, String status) {
        Board board = findById(id);
        board.setQnaStatus(status);
    }

    @Transactional
    public void updateReportStep(Long id, String step) {
        Board board = findById(id);
        board.setReportStep(step);
    }
}

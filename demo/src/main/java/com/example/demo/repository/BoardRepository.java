package com.example.demo.repository;

import com.example.demo.entity.Board;
import com.example.demo.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByBoardTypeOrderByIdDesc(BoardType boardType);

    @Query("SELECT COUNT(b) FROM Board b WHERE b.boardType = :boardType")
    long countByBoardType(BoardType boardType);

    List<Board> findAllByOrderByIdDesc();
}

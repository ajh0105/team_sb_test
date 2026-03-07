package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "boards")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @Builder.Default
    private int views = 0;

    // QNA 전용 - 답변 상태
    private String qnaStatus; // "답변대기", "답변완료"

    // REPORT 전용 - 처리 상태
    private String reportStep; // "접수", "조사중", "완료"

    // REPORT 전용 - 비밀글 여부
    @Column(nullable = false)
    @Builder.Default
    private boolean secret = false;

    public void increaseViews() {
        this.views++;
    }
}

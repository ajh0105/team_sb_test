package com.example.tour.service;

import com.example.tour.domain.Member;
import com.example.tour.domain.MemberRole;
import com.example.tour.dto.MemberFormDto;
import com.example.tour.repository.MemberRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Long save(MemberFormDto dto) {

        Member member = Member.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .name(dto.getName())
                .email(dto.getEmail())
                .role(MemberRole.USER)
                .build();

        return memberRepository.save(member).getId();
    }

    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    /**
     * 아이디 중복 체크
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    /**
     * 로그인 처리
     */
    @Transactional(readOnly = true)
    public Member login(String username, String password) {
        // 1. 아이디로 회원 조회
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        // 2. 비밀번호 일치 여부 확인
        // (주의: 현재 코드는 평문 비밀번호를 비교합니다. 실무에서는 PasswordEncoder를 사용해야 합니다.)
        if (!member.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }

}
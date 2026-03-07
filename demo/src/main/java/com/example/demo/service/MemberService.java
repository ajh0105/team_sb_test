package com.example.demo.service;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.MemberFormDto;
import com.example.demo.entity.Member;
import com.example.demo.entity.Role;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 처리 (DTO 방식)
     */
    @Transactional
    public Long join(MemberFormDto dto) {
        // 이메일 중복 체크
        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 BCrypt 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        Member member = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encodedPassword)
                .phone(dto.getPhone())
                .role(Role.USER)
                .build();

        Member savedMember = memberRepository.save(member);
        log.info("회원가입 완료: ID {}, Email: {}", savedMember.getId(), savedMember.getEmail());
        return savedMember.getId();
    }

    /**
     * 로그인 처리 (DTO 방식)
     */
    public Member login(LoginDto dto) {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        // BCrypt 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        log.info("로그인 성공 - Email: {}", dto.getEmail());
        return member;
    }

    /**
     * 이메일 중복 여부 확인 (Ajax 등에서 사용)
     */
    public boolean existsByEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }
}

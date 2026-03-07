package com.example.demo.service;

import com.example.demo.entity.Member;
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
     * 회원 가입 처리
     * DTO 없이 파라미터를 직접 받아 처리하도록 수정했습니다.
     */
    @Transactional
    public Long join(String name, String email, String password, String phone) {
        // 이메일 중복 체크
        if(memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 엔티티 생성 및 데이터 세팅 (이름, 이메일, 비번, 전화번호)
        Member member = new Member();
        member.setEmail(email);
        member.setPassword(encodedPassword);
        member.setPhone(phone);

        Member savedMember = memberRepository.save(member);

        log.info("회원가입 완료: ID {}, Email: {}", savedMember.getId(), savedMember.getEmail());
        return savedMember.getId();
    }

    /**
     * 로그인 처리
     */
    public Member login(String email, String password) {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.")
        );

        // 비밀번호 일치 확인
        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        log.info("로그인 성공 - Email: {}", email);
        return member;
    }
}
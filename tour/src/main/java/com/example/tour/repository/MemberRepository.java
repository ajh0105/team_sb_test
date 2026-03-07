package com.example.tour.repository;

import com.example.tour.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 아이디(username)로 회원 정보 조회용 (로그인 시 필요)
    Optional<Member> findByUsername(String username);

    // 아이디(username) 중복 여부 확인용 (Ajax 체크 시 필요)
    boolean existsByUsername(String username);
}

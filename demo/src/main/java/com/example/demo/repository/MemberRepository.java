package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // DB에서 이메일로 회원을 찾는 기능
    Optional<Member> findByEmail(String email);
}
package com.ohgiraffers.comprehensive.member.service;

import com.ohgiraffers.comprehensive.member.domain.Member;
import com.ohgiraffers.comprehensive.member.domain.repository.MemberRepository;
import com.ohgiraffers.comprehensive.member.dto.request.MemberSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor    // 필요한 생성자 만들기
@Transactional // save 잘 처리 될 수 있도록 트랜잭션 처리
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /* 1. 회원 가입 */
    public void signup(final MemberSignupRequest memberRequest) {

        final Member newMember = Member.of(
                memberRequest.getMemberId(),
                passwordEncoder.encode(memberRequest.getMemberPassword()),
                memberRequest.getMemberName(),
                memberRequest.getMemberEmail()
        );

        memberRepository.save(newMember); // newMember를 save 해달라
    }
}

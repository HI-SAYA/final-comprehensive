package com.ohgiraffers.comprehensive.member.controller;

import com.ohgiraffers.comprehensive.member.dto.request.MemberSignupRequest;
import com.ohgiraffers.comprehensive.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor // 생성자를 이용한 의존성 주입
public class MemberController {

    private final MemberService memberService;

    /* 1. 회원 가입 */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid MemberSignupRequest memberRequest) {

        memberService.signup(memberRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}

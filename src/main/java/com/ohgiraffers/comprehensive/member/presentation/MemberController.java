package com.ohgiraffers.comprehensive.member.presentation;

import com.ohgiraffers.comprehensive.member.dto.request.MemberSignupRequest;
import com.ohgiraffers.comprehensive.member.dto.response.ProfileResponse;
import com.ohgiraffers.comprehensive.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping({"/member", "/api/v1/member"})
@RequiredArgsConstructor // 생성자를 이용한 의존성 주입
public class MemberController {

    private final MemberService memberService;

    /* 1. 회원 가입 */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid MemberSignupRequest memberRequest) {

        memberService.signup(memberRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /* 2. 프로필 조회 */
    @GetMapping // @GetMapping이라고 해두면 /api/v1/member와 연결된다. -> 인증필터에서 인증되고나서 동작한다.
    public ResponseEntity<ProfileResponse> profile(@AuthenticationPrincipal User user) {

        ProfileResponse profileResponse = memberService.getProfile(user.getUsername());

        return ResponseEntity.ok(profileResponse);
    }

}

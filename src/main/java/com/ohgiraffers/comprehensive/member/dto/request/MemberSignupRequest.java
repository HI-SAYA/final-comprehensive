package com.ohgiraffers.comprehensive.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor // 의존성 주입 의도, 아래 필드 값들을 전달받는 생성자를 만든다. 라는 의미
public class MemberSignupRequest {

    @NotBlank
    private final String memberId;
    @NotBlank
    private final String memberPassword;
    @NotBlank
    private final String memberName;
    private final String memberEmail;
}

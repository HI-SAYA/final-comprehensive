package com.ohgiraffers.comprehensive.jwt;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User; // userDetails 상속 필수
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUser extends User {

    private final Long memberCode;

    public CustomUser(Long memberCode, UserDetails userDetails) {
        super(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        this.memberCode = memberCode;
    }

    public static CustomUser of(Long memberCode, UserDetails userDetails) {
        return new CustomUser(
                memberCode,
                userDetails
        ); // 외부에서 생성자 받아오지 않기 위해 이런 방법 사용
    }
}

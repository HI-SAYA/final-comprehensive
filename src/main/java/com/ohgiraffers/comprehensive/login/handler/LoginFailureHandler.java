package com.ohgiraffers.comprehensive.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.comprehensive.common.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode.FAIL_LOGIN;

/* 로그인 실패 처리 핸들러 */
@RequiredArgsConstructor // 의존성 주인 (objectMapper)
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new ExceptionResponse(FAIL_LOGIN)));
        // getWriter 출력 스트림 / 자바 객체 -> json String으로 변환하는 코드이다.
    }
}

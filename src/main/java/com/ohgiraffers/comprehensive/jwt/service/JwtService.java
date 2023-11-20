package com.ohgiraffers.comprehensive.jwt.service;

import com.ohgiraffers.comprehensive.common.exception.BadRequestException;
import com.ohgiraffers.comprehensive.common.exception.NotFoundException;
import com.ohgiraffers.comprehensive.jwt.CustomUser;
import com.ohgiraffers.comprehensive.member.domain.Member;
import com.ohgiraffers.comprehensive.member.domain.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode.NOT_FOUND_MEMBER_ID;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    private final Key key;
    private final MemberRepository memberRepository;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String BEARER = "Bearer ";

    public JwtService(@Value("${jwt.secret}") String secretKey, MemberRepository memberRepository) { // yml에서 비밀 키를 읽어왔다.
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);      // byte 배열 형태로 만든다.
        this.key = Keys.hmacShaKeyFor(keyBytes);                  // 전달하면서 인증키를 만든다.
        this.memberRepository = memberRepository;
    }

    public String createAccessToken(Map<String, String> memberInfo) {

        Claims claims = Jwts.claims().setSubject(ACCESS_TOKEN_SUBJECT);
        claims.putAll(memberInfo);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationPeriod)) // 만료 시간 설정 = 현재시간 +
                .signWith(key, SignatureAlgorithm.HS512)   // 서명
                .compact();
    }

    public String createRefreshToken() {

        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationPeriod))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /* refresh Token DB 저장 */
    @Transactional // 실제 Entity 가져와서 바꾼거라 Transaction 필수
    public void updateRefreshToken(String memberId, String refreshToken) {
        memberRepository.findByMemberId(memberId)
                .ifPresentOrElse(
                        member -> member.updateRefreshToken(refreshToken),
                        () -> new NotFoundException(NOT_FOUND_MEMBER_ID)
                );
    }

    public Optional<String> getRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Refresh-Token"))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> getAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Access-Token"))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
                // 액세스 토큰이 있으면 필터링을 하는데 Bearer XXXXX 넘어온다.
                // BEARER를 제거하고 쓰겠다. (replace)
    }

    public boolean isValidToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        memberRepository.findByRefreshToken(refreshToken)
                .ifPresent(member -> {
                    String reIssueRefreshToken = reIssueRefreshToken(member);
                    String accessToken = createAccessToken(
                            Map.of("memberId", member.getMemberId(), "memberRole", member.getMemberRole().name())
                    );
                    response.setHeader("Access-Token", accessToken);
                    response.setHeader("Refresh-Token", reIssueRefreshToken);
                });
    }

    private String reIssueRefreshToken(Member member) {
        String reIssueRefreshToken = createRefreshToken();
        member.updateRefreshToken(reIssueRefreshToken);
        memberRepository.saveAndFlush(member);
        return reIssueRefreshToken;
    }


    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        getAccessToken(request)
                .filter(this::isValidToken) // 유효한 지 판단
                .ifPresent(accessToken -> getMemberId(accessToken) // 유효하다고 판단했을 경우 - memberId를 꺼내온다.
                                .ifPresent(memberId -> memberRepository.findByMemberId(memberId)
                                        .ifPresent(this::saveAuthentication))); // 문제가 없다면 여기까지 도달한 것

        filterChain.doFilter(request, response);
        
    }




    private Optional<String> getMemberId(String accessToken) {
        try {
            return Optional.ofNullable(
                    Jwts.parserBuilder() // Jwts에서부터 파싱처리할 수 있는 객체를 불러온다.
                            .setSigningKey(key) // 서명키
                            .build()
                            .parseClaimsJws(accessToken) // Claims = payload 정보 몸체
                            .getBody()            // Body를 얻으면 그 안에 Claims을 얻는다.
                            .get("memberId").toString()     // memberId로 선별
            );

        } catch (Exception e) {
            log.error("Access Token이 유효하지 않습니다.");
            return Optional.empty();
            // accessToken 안에 MemberId를 꺼내온다. 없을 경우 문제가 생기니까 Exception 핸들링을 미리 해놓았다.
        }
    }

    /* ******* 중요 */
    public void saveAuthentication(Member member) {

        UserDetails userDetails = User.builder()
                .username(member.getMemberId())
                .password(member.getMemberPassword())
                .roles(member.getMemberRole().name())
                .build();

        CustomUser customUser = CustomUser.of(member.getMemberCode(), userDetails);

//        Authentication authentication
//                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // 생성자에 3개의 인자 전달 principal(현재 인증된 사용자),
        // credentials(세팅할 필요 없어서 null, 로그인 체크 여부),
        // authority 인증-> 인가 처리 때문에 getAuthorities() 써야 한다.(user인지 admin인지 체크)
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
        // 커스텀한 인증 객체를 저장하여 사용한다. (memberCode를 더하여 저장하기 위해 이렇게 처리함)

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

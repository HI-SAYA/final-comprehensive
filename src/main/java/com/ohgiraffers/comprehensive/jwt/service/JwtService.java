package com.ohgiraffers.comprehensive.jwt.service;

import com.ohgiraffers.comprehensive.common.exception.BadRequestException;
import com.ohgiraffers.comprehensive.member.domain.Member;
import com.ohgiraffers.comprehensive.member.domain.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                        () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
                );
    }

    public Optional<String> getRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Refresh-Token"))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
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


}

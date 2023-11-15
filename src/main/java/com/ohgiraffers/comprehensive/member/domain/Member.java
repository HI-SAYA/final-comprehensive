package com.ohgiraffers.comprehensive.member.domain;

import com.ohgiraffers.comprehensive.member.domain.type.MemberRole;
import com.ohgiraffers.comprehensive.member.domain.type.MemberStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.ohgiraffers.comprehensive.member.domain.type.MemberRole.USER;
import static com.ohgiraffers.comprehensive.member.domain.type.MemberStatus.ACTIVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_member")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class) // 날짜 감지 -> 자동 생성
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY) // PK
    private Long memberCode;

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String memberPassword;

    @Column(nullable = false)
    private String memberName;

    private String memberEmail;

    @Column(nullable = false)
    @Enumerated(value = STRING)     // 숫자대신 문자열로 받기
    private MemberRole memberRole = USER;

    @CreatedDate    // jpa에서 감지된다. 해당 행이 수정된 순간 자동으로 처리해준다.
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // ..
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private MemberStatus status = ACTIVE;

    private String refreshToken;

    public Member(String memberId, String memberPassword, String memberName, String memberEmail) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
    }

    public static Member of(String memberId, String memberPassword, String memberName, String memberEmail) {
        // 매개변수로 전달된 값을 엔티티로 만들어주는 역할 = of
        // 만들어서 반환
        return new Member (
                memberId,
                memberPassword,
                memberName,
                memberEmail
        ); // 생성자 만들어 준다. (위에 this.memberId = memberId; ....)
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

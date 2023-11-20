package com.ohgiraffers.comprehensive.review.domain;

import com.ohgiraffers.comprehensive.common.domain.BaseEntity;
import com.ohgiraffers.comprehensive.member.domain.Member;
import com.ohgiraffers.comprehensive.product.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_review")
@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "status = 'USABLE'")
//@SQLDelete(sql = "UPDATE tbl_review SET status = 'DELETED' WHERE reviewCode = ?")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long reviewCode;

    @ManyToOne
    @JoinColumn(name = "productCode")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "memberCode")
    private Member member;

    @Column(nullable = false)
    private String reviewTitle;

    @Column(nullable = false)
    private String reviewContent;

    public Review(Product product, Member member, String reviewTitle, String reviewContent) {
        this.product = product;
        this.member = member;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
    }

    public static Review of(Product product, Member member, String reviewTitle, String reviewContent) {

        return new Review(
                product,
                member,
                reviewTitle,
                reviewContent
        );

    }
}
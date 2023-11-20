package com.ohgiraffers.comprehensive.review.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ohgiraffers.comprehensive.review.domain.Review;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReviewResponse {

    private final Long reviewCode;
    private final String productName;
    private final String memberName;
    private final String reviewTitle;
    private final String reviewContent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime modifiedAt;

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getReviewCode(),
                review.getProduct().getProductName(),
                review.getMember().getMemberName(),
                review.getReviewTitle(),
                review.getReviewContent(),
                review.getCreatedAt(),
                review.getModifiedAt()
        );
    }
}
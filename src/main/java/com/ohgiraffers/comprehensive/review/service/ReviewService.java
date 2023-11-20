package com.ohgiraffers.comprehensive.review.service;

import com.ohgiraffers.comprehensive.common.exception.ConflictException;
import com.ohgiraffers.comprehensive.common.exception.NotFoundException;
import com.ohgiraffers.comprehensive.jwt.CustomUser;
import com.ohgiraffers.comprehensive.member.domain.Member;
import com.ohgiraffers.comprehensive.member.domain.repository.MemberRepository;
import com.ohgiraffers.comprehensive.order.domain.repository.OrderRepository;
import com.ohgiraffers.comprehensive.product.domain.Product;
import com.ohgiraffers.comprehensive.product.domain.repository.ProductRepository;
import com.ohgiraffers.comprehensive.review.domain.Review;
import com.ohgiraffers.comprehensive.review.dto.request.ReviewCreateRequest;
import com.ohgiraffers.comprehensive.review.dto.response.ReviewResponse;
import com.ohgiraffers.comprehensive.review.dto.response.ReviewsResponse;
import com.ohgiraffers.comprehensive.review.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("reviewCode").descending());
    }

    @Transactional(readOnly = true) // 순수한 조회 기능일 때 readOnly
    public Page<ReviewsResponse> getReviews(final int page, final Long productCode) {

        final Page<Review> reviews = reviewRepository.findByProductProductCode(getPageable(page), productCode);

        return reviews.map(review -> ReviewsResponse.from(review));
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long reviewCode) {

        final Review review = reviewRepository.findById(reviewCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_REVIEW_CODE));

        return ReviewResponse.from(review);
    }

    @Transactional(readOnly = true)
    public void validateProductOrder(Long productCode, CustomUser customUser) {

        if(!orderRepository.existsByProductProductCodeAndMemberCode(productCode, customUser.getMemberCode())) {
            throw new NotFoundException(NOT_FOUND_VALID_ORDER);
        }
    }

    @Transactional(readOnly = true)
    public void validateReviewCreate(Long productCode, CustomUser customUser) {

        if(reviewRepository.existsByProductProductCodeAndMemberMemberCode(productCode, customUser.getMemberCode())) {
            throw new ConflictException(ALREADY_EXIST_REVIEW);
        }
    }

    public Long save(ReviewCreateRequest reviewRequest, CustomUser customUser) {

        Product product = productRepository.findById(reviewRequest.getProductCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT_CODE));

        Member member = memberRepository.findById(customUser.getMemberCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER_CODE));

        final Review newReview = Review.of(
                product,
                member,
                reviewRequest.getReviewTitle(),
                reviewRequest.getReviewContent()
        );

        final Review review = reviewRepository.save(newReview);

        return review.getReviewCode();

    }
}

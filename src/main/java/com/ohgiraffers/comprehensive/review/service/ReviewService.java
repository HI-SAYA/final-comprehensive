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
        // reviewRequest에서 얻은 제품코드 getProductCode()를 사용하여
        // 데이터 베이스에서 Product 엔티티를 찾는다.
        // 첫줄 - 해당 제품 코드를 가진 Product 엔터티를 DB에서 찾기 위해 productRepository를 사용한다.
        // findById 메서드는 주어진 ID에 해당하는 엔티티를 찾아 반환하거나, 찾지 못하면
        // 비어있는 Optional을 반환한다.
        // .orElseThrow(() -> new NotFound~ 만약 findById가 비어있는 Optinal을 반환한다면,
        // orElseThrow 메서드를 사용하여 예외를 던진다. 여기서는 NotFoundException을 던지고,
        // 해당 예외에는 NOT_FOUND_PRODUCT_CODE라는 메세지를 포함한다.
        // 이것은 주로 특정 제품 코드에 해당하는 제품이 DB에 없을 경우에 예외를 발생시키고,
        // 해당 예외를 적절히 처리하는 곳에서 처리하도록 하는 용도로 사용된다.
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

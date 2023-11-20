package com.ohgiraffers.comprehensive.review.domain.repository;

import com.ohgiraffers.comprehensive.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"product", "member"}) // Review의 필드명
    Page<Review> findByProductProductCode(Pageable pageable, Long productCode);

    boolean existsByProductProductCodeAndMemberMemberCode(Long productCode, Long memberCode);
}
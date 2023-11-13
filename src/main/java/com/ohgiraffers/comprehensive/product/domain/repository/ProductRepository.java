package com.ohgiraffers.comprehensive.product.domain.repository;

import com.ohgiraffers.comprehensive.product.domain.Product;
import com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/* 연관 관계 매핑 시 연관 대상 entity의 pk별로 한 번씩 구문이 발생하는 N + 1 문제가 있다.
* 해당 필드 미사용 : FetchType.LAZY -> 아예 조회 하지 않음
* 해당 필드 사용 : fetch join (JPQL), @EntityGraph (Query Method) -> join해서 가져옴
* */
public interface ProductRepository extends JpaRepository<Product, Long> {
    // <> 엔티티, 타입

    /* 1. 상품 목록 조회 : 페이징, 주문 불가 상품 제외 (고객) */
    Page<Product> findByStatus(Pageable pageable, ProductStatusType productStatusType);

    /* 2. 상품 목록 조회 : 페이징, 주문 불가 상품 포함 (관리자) */
    @EntityGraph(attributePaths = {"category"})
    Page<Product> findByStatusNot(Pageable pageable, ProductStatusType productStatusType);

    /* 3. 상품 목록 조회 : 카테고리 기준, 페이징, 주문 불가 상품 제외(고객) */
    Page<Product> findByCategoryCategoryCodeAndStatus(Pageable pageable, Long categoryCode, ProductStatusType productStatusType);
    // Product 안에 있는 필드명 Category의 CategoryCode

    /* 4. 상품 목록 조회 : 상품명 검색 기준, 페이징, 주문 불가 상품 제외 (고객) */
    Page<Product> findByProductNameContainsAndStatus(Pageable pageable, String productName, ProductStatusType productStatusType);

    /* 5. 상품 상세 조회 : productCode로 상품 1개 조회, 주문 불가 상품 제외(고객) */
    Optional<Product> findByProductCodeAndStatus(Long productCode, ProductStatusType productStatusType);
    // NPE 방지하고자 Optional 사용

    /* 6. 상품 상세 조회 : productCode로 상품 1개 조회, 주문 불가 상품 포함(관리자) */
    @EntityGraph(attributePaths = {"category"})
    Optional<Product> findByProductCodeAndStatusNot(Long productCode, ProductStatusType productStatusType);

    /* 7. 상품 등록(관리자) */

    /* 8. 상품 수정(관리자) */

    /* 9. 상품 삭제(관리자) */
}

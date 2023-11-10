package com.ohgiraffers.comprehensive.product.service;

import com.ohgiraffers.comprehensive.product.domain.Product;
import com.ohgiraffers.comprehensive.product.domain.repository.ProductRepository;
import com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType;
import com.ohgiraffers.comprehensive.product.dto.response.AdminProductsResponse;
import com.ohgiraffers.comprehensive.product.dto.response.CustomerProductsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType.DELETED;
import static com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType.USABLE;

@Service
@RequiredArgsConstructor // final이 붙인 생성자 argu를 던져주는 어노테이션
public class ProductService {

    private final ProductRepository productRepository;

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page -1, 10, Sort.by("productCode").descending());
    }

    /* 1. 상품 목록 조회 : 페이징, 주문 불가 상품 제외 (고객) */
    @Transactional(readOnly = true)
    public Page<CustomerProductsResponse> getCustomerProducts(final Integer page) {

        Page<Product> products = productRepository.findByStatus(getPageable(page), USABLE);
        // Product Entity가 노출되면 위험하기 때문에 Page<>에서 내보낼 때 dto 타입이어야 한다.

        return products.map(product -> CustomerProductsResponse.from(product));
    }

    /* 2. 상품 목록 조회 : 페이징, 주문 불가 상품 포함 (관리자) */
    @Transactional(readOnly = true)
    public Page<AdminProductsResponse> getAdminProducts(final Integer page) {

        Page<Product> products = productRepository.findByStatusNot(getPageable(page), DELETED);

        return products.map(product -> AdminProductsResponse.from(product));
    }
}

package com.ohgiraffers.comprehensive.product.presentation;

import com.ohgiraffers.comprehensive.common.paging.Pagenation;
import com.ohgiraffers.comprehensive.common.paging.PagingButtonInfo;
import com.ohgiraffers.comprehensive.common.paging.PagingResponse;
import com.ohgiraffers.comprehensive.product.dto.response.AdminProductsResponse;
import com.ohgiraffers.comprehensive.product.dto.response.CustomerProductsResponse;
import com.ohgiraffers.comprehensive.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    /* 1. 상품 목록 조회 - 페이징, 주문 불가 상품 제외 (고객) */
    @GetMapping("/products")
    public ResponseEntity<PagingResponse> getCustomerProducts(@RequestParam(defaultValue = "1") Integer page) {

        final Page<CustomerProductsResponse> products = productService.getCustomerProducts(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(products);
        final PagingResponse pagingResponse = PagingResponse.of(products.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /* 2. 상품 목록 조회 - 페이징, 주문 불가 상품 제외 (관리자) */
    @GetMapping("/products-management")
    public ResponseEntity<PagingResponse> getAdminProducts(@RequestParam(defaultValue = "1") Integer page) {

        final Page<AdminProductsResponse> products = productService.getAdminProducts(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(products);
        final PagingResponse pagingResponse = PagingResponse.of(products.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }
}

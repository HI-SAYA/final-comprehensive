package com.ohgiraffers.comprehensive.product.dto.response;

import com.ohgiraffers.comprehensive.product.domain.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CustomerProductsResponse {

    private final Long productCode;
    private final String productName;
    private final Long productPrice;
    private final String productImageUrl;

    public static CustomerProductsResponse from(final Product product) {
        return new CustomerProductsResponse(
                product.getProductCode(),
                product.getProductName(),
                product.getProductPrice(),
                product.getProductImageUrl()
        );
    }
}

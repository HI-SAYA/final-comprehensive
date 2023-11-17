package com.ohgiraffers.comprehensive.product.dto.response;

import com.ohgiraffers.comprehensive.product.domain.Product;
import com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class AdminProductResponse {

    private final Long productCode;
    private final String productName;
    private final Long productPrice;
    private final String productDescription;
    private final Long categoryCode;
    private final String categoryName;
    private final String productImageUrl;
    private final Long productStock;
    private final ProductStatusType status;

    public static AdminProductResponse from(final Product product) {
        return new AdminProductResponse(
                product.getProductCode(),
                product.getProductName(),
                product.getProductPrice(),
                product.getProductDescription(),
                product.getCategory().getCategoryCode(),
                product.getCategory().getCategoryName(),
                product.getProductImageUrl(),
                product.getProductStock(),
                product.getStatus()
        );
    }
}

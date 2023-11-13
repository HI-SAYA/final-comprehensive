package com.ohgiraffers.comprehensive.product.dto.response;

import com.ohgiraffers.comprehensive.product.domain.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CustomerProductResponse {

        private final Long productCode;
        private final String productName;
        private final Long productPrice;
        private final String productDescription;
        private final String productImageUrl;
        private final Long productStock;

        public static CustomerProductResponse from(final Product product) {
            return new CustomerProductResponse(
                    product.getProductCode(),
                    product.getProductName(),
                    product.getProductPrice(),
                    product.getProductDescription(),
                    product.getProductImageUrl(),
                    product.getProductStock()
            );
        }
    }


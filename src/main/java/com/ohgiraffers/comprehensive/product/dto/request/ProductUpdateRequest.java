package com.ohgiraffers.comprehensive.product.dto.request;

import com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
public class ProductUpdateRequest {

    @NotBlank
    private final String productName;
    @Min(value = 1)
    private final Long productPrice;
    @NotBlank
    private final String productDescription;
    @Min(value = 1)
    private final Long categoryCode;
    @Min(value = 1)
    private final Long productStock;
    @NotNull
    private final ProductStatusType status; // 잠깐 판매중지 하고 싶으면 사용

}

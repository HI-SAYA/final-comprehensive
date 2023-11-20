package com.ohgiraffers.comprehensive.order.dto.reqeust;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class OrderCreateRequest {

    @Min(value = 1)
    private final Long productCode;
    @NotBlank
    private final String orderPhone;
    @NotBlank
    private final String orderEmail;
    @NotBlank
    private final String orderReceiver;
    @NotBlank
    private final String orderAddress;
    @Min(value = 1)
    private final Long orderAmount;
    // insert 할 때 반드시 클라이언트로부터 받아 와야 할 정보를 잘 생각 해서 선별 해야 한다.
}

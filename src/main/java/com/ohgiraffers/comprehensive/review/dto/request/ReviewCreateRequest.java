package com.ohgiraffers.comprehensive.review.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class ReviewCreateRequest {

    @Min(value = 1)
    private final Long productCode;
    @NotBlank
    private final String reviewTitle;
    @NotBlank
    private final String reviewContent;
}

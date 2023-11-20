package com.ohgiraffers.comprehensive.common.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    FAIL_TO_UPLOAD_FILE(1001, "파일 저장에 실패하였습니다."),
    FAIL_TO_DELETE_FILE(1002, "파일 삭제에 실패하였습니다."),

    NOT_FOUND_CATEGORY_CODE(2000, "카테고리 코드에 해당하는 카테고리가 존재하지 않습니다."),

    NOT_FOUND_PRODUCT_CODE(3000, "상품 코드에 해당하는 상품이 존재하지 않습니다."),

    FAIL_LOGIN(4000, "로그인에 실패하였습니다."),
    UNAUTHORIZED(4001, "인증 되지 않은 요청입니다."),
    NOT_FOUND_MEMBER_ID(4002,"아이디에 해당하는 유저가 없습니다."),
    ACCESS_DENIED(4003, "허가 되지 않은 요청입니다."),
    NOT_FOUND_MEMBER_CODE(4004, "멤버 코드에 해당하는 유저가 없습니다."),

    NOT_ENOUGH_STOCK(5000, "재고 부족으로 주문 불가합니다."),
    NOT_FOUND_VALID_ORDER(5001, "유효한 주문 건이 없습니다."),

    NOT_FOUND_REVIEW_CODE(6000, "리뷰 코드에 해당하는 리뷰가 존재하지 않습니다."),
    ALREADY_EXIST_REVIEW(6001, "이미 리뷰가 작성되어 작성할 수 없습니다.");

    private final int code;
    private final String message;
}

// 우리 프로그램에서 발생하는 exceptionCode를 쭉 나열하고 싶다.
// NOT_FOUND_CATEGORY_CODE, NOT_FOUND_PRODUCT_CODE 등 나열할 수 있다.
// 내부적인 값 또한 작성할 수 있다.(message)
// 1. 필드로 선언을 먼저 한 다음에 2. 그것에 대한 생성자를 만들고, 3. code와 message 값을 세팅한다.

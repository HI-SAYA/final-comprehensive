package com.ohgiraffers.comprehensive.common.paging;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PagingButtonInfo {

    private final int currentPage;
    private final int startPage;
    private final int endPage;
    private final int maxPage;


}
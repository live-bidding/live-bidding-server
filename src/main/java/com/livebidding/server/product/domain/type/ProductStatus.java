package com.livebidding.server.product.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    PREPARED("준비중", "경매 시작 전 상태입니다."),
    IN_PROGRESS("진행중", "경매가 활발하게 진행 중인 상태입니다."),
    CLOSED("마감", "경매가 종료된 상태입니다.");

    private final String title;
    private final String description;
}

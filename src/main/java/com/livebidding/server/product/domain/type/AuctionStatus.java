package com.livebidding.server.product.domain.type;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuctionStatus {
    SCHEDULED("경매 예정"),
    IN_PROGRESS("경매 진행중"),
    ENDED("경매 종료");

    private final String description;

    public static AuctionStatus determineStatus(LocalDateTime now, LocalDateTime startTime, LocalDateTime endTime) {
        if (now.isBefore(startTime)) {
            return SCHEDULED;
        }
        if (now.isBefore(endTime)) {
            return IN_PROGRESS;
        }
        return ENDED;
    }
}

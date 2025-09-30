package com.livebidding.server.product.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuctionPeriod {

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private AuctionPeriod(final LocalDateTime startTime, final LocalDateTime endTime) {
        validate(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static AuctionPeriod of(final LocalDateTime startTime, final LocalDateTime endTime) {
        return new AuctionPeriod(startTime, endTime);
    }

    private void validate(final LocalDateTime startTime, final LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("[ERROR] 경매 시간은 null일 수 없습니다.");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("[ERROR] 경매 종료 시간은 시작 시간보다 이후여야 합니다.");
        }
    }
}

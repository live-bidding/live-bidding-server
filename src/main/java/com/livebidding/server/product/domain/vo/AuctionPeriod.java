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

    /**
     * Create an AuctionPeriod with the given start and end times.
     *
     * Validates that neither `startTime` nor `endTime` is null and that `endTime` is not before `startTime`.
     *
     * @param startTime the auction start time
     * @param endTime the auction end time
     * @throws IllegalArgumentException if either time is null or if `endTime` is before `startTime`
     */
    private AuctionPeriod(final LocalDateTime startTime, final LocalDateTime endTime) {
        validate(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Create an AuctionPeriod representing the provided start and end times.
     *
     * @param startTime the auction start time; must not be null
     * @param endTime   the auction end time; must not be null and must not be before {@code startTime}
     * @return          the created AuctionPeriod
     * @throws IllegalArgumentException if {@code startTime} or {@code endTime} is null, or if {@code endTime} is before {@code startTime}
     */
    public static AuctionPeriod of(final LocalDateTime startTime, final LocalDateTime endTime) {
        return new AuctionPeriod(startTime, endTime);
    }

    /**
     * Validates that both auction times are present and that the end time is not before the start time.
     *
     * @param startTime the auction start time
     * @param endTime   the auction end time
     * @throws IllegalArgumentException if either `startTime` or `endTime` is null, or if `endTime` is before `startTime`
     */
    private void validate(final LocalDateTime startTime, final LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("[ERROR] 경매 시간은 null일 수 없습니다.");
        }
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("[ERROR] 경매 종료 시간은 시작 시간보다 이전일 수 없습니다.");
        }
    }
}

package com.livebidding.server.product.domain.vo;

import com.livebidding.server.product.exception.ProductErrorCode;
import com.livebidding.server.product.exception.ProductException;
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
            throw new ProductException(ProductErrorCode.AUCTION_TIME_CANNOT_BE_NULL);
        }
        if (!endTime.isAfter(startTime)) {
            throw new ProductException(ProductErrorCode.INVALID_AUCTION_PERIOD);
        }
    }
}

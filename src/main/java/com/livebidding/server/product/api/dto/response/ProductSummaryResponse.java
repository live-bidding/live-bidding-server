package com.livebidding.server.product.api.dto.response;

import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.product.domain.type.AuctionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductSummaryResponse(
        Long id,
        String name,
        BigDecimal currentPrice,
        LocalDateTime auctionEndTime,
        AuctionStatus status
) {
    public static ProductSummaryResponse from(Product product) {
        return new ProductSummaryResponse(
                product.getId(),
                product.getName(),
                product.getCurrentPrice(),
                product.getAuctionEndTime(),
                product.getStatus()
        );
    }
}

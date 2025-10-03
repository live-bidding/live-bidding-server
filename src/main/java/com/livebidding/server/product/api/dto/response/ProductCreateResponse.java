package com.livebidding.server.product.api.dto.response;

import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.product.domain.type.AuctionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductCreateResponse(
        Long id,
        String name,
        AuctionStatus status,
        BigDecimal currentPrice,
        LocalDateTime auctionStartTime,
        LocalDateTime auctionEndTime
) {
    public static ProductCreateResponse from(Product product) {
        return new ProductCreateResponse(
                product.getId(),
                product.getName(),
                product.getStatus(),
                product.getCurrentPrice(),
                product.getAuctionStartTime(),
                product.getAuctionEndTime()
        );
    }
}

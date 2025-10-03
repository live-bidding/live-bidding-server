package com.livebidding.server.product.api.dto.response;

import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.product.domain.type.AuctionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record ProductDetailResponse(
        Long id,
        String name,
        String description,
        BigDecimal startPrice,
        BigDecimal currentPrice,
        LocalDateTime auctionStartTime,
        LocalDateTime auctionEndTime,
        AuctionStatus status,
        SellerResponse seller
) {
    public static ProductDetailResponse from(Product product) {
        return new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getStartPrice(),
                product.getCurrentPrice(),
                product.getAuctionStartTime(),
                product.getAuctionEndTime(),
                product.getStatus(),
                SellerResponse.from(product.getSeller())
        );
    }
}

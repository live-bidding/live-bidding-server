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
        SellerResponse seller,
        List<?> bids // 1.3 입찰 기능 구현 후, 실제 입찰 내역 DTO로 변경 예정
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
                SellerResponse.from(product.getSeller()),
                Collections.emptyList() // 현재는 빈 리스트 반환
        );
    }
}

package com.livebidding.server.product.api.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductCreateRequest(
        @NotBlank(message = "상품명은 필수입니다.")
        String name,

        String description,

        @NotNull(message = "시작 가격은 필수입니다.")
        @Positive(message = "시작 가격은 0보다 커야 합니다.")
        BigDecimal startPrice,

        @NotNull(message = "경매 시작 시간은 필수입니다.")
        @Future(message = "경매 시작 시간은 현재 시간보다 미래여야 합니다.")
        LocalDateTime auctionStartTime,

        @NotNull(message = "경매 종료 시간은 필수입니다.")
        LocalDateTime auctionEndTime
) {
}

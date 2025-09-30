package com.livebidding.server.product.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuctionPeriodTest {

    @Test
    @DisplayName("유효한 시간으로 AuctionPeriod 객체를 생성한다.")
    void create_auction_period_successfully() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(1);

        // when
        AuctionPeriod auctionPeriod = AuctionPeriod.of(startTime, endTime);

        // then
        assertThat(auctionPeriod.getStartTime()).isEqualTo(startTime);
        assertThat(auctionPeriod.getEndTime()).isEqualTo(endTime);
    }

    @Test
    @DisplayName("종료 시간이 시작 시간보다 이전일 경우 예외를 던진다.")
    void throw_exception_when_endTime_is_before_startTime() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.minusDays(1);

        // when & then
        assertThatThrownBy(() -> AuctionPeriod.of(startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 경매 종료 시간은 시작 시간보다 이후여야 합니다.");
    }

    @Test
    @DisplayName("종료 시간과 시작 시간이 같을 경우 예외를 던진다.")
    void throw_exception_when_endTime_is_equal_to_startTime() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> AuctionPeriod.of(now, now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 경매 종료 시간은 시작 시간보다 이후여야 합니다.");
    }
}

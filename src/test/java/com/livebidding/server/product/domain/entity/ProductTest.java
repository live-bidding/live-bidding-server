package com.livebidding.server.product.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.livebidding.server.product.domain.type.AuctionStatus;
import com.livebidding.server.product.exception.ProductException;
import com.livebidding.server.support.TestFixture;
import com.livebidding.server.user.domain.entity.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("상품 생성에 성공한다.")
    void createSuccess() {
        // given
        User seller = TestFixture.createUser();
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(2);

        // when
        Product product = Product.of("테스트 상품", "설명", BigDecimal.valueOf(1000), startTime, endTime, seller);

        // then
        assertThat(product.getName()).isEqualTo("테스트 상품");
        assertThat(product.getStatus()).isEqualTo(AuctionStatus.SCHEDULED);
    }

    @Test
    @DisplayName("경매 시작 시간이 현재보다 과거일 경우 상품 생성에 실패한다.")
    void createFail_InvalidStartTime() {
        // given
        User seller = TestFixture.createUser();
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(2);

        // when & then
        assertThatThrownBy(() -> Product.of("상품명", "설명", BigDecimal.valueOf(1000), startTime, endTime, seller))
                .isInstanceOf(ProductException.class);
    }
}
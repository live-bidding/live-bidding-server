package com.livebidding.server.product.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.livebidding.server.product.exception.ProductErrorCode;
import com.livebidding.server.product.exception.ProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {
    @Test
    @DisplayName("0 이상의 값으로 Price 객체를 생성한다.")
    void create_price_successfully() {
        // when
        Price price = Price.from(1000L);
        // then
        assertThat(price.getValue()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("가격이 null일 경우 ProductException을 던진다.")
    void throw_exception_when_price_is_null() {
        assertThatThrownBy(() -> Price.from((Long) null))
                .isInstanceOf(ProductException.class)
                .hasFieldOrPropertyWithValue("errorCode", ProductErrorCode.PRICE_CANNOT_BE_NULL);
    }

    @Test
    @DisplayName("가격이 음수일 경우 ProductException을 던진다.")
    void throw_exception_when_price_is_negative() {
        // when & then
        assertThatThrownBy(() -> Price.from(-100L))
                .isInstanceOf(ProductException.class)
                .hasFieldOrPropertyWithValue("errorCode", ProductErrorCode.PRICE_CANNOT_BE_NEGATIVE);
    }
}

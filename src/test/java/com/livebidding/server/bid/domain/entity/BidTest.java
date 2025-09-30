package com.livebidding.server.bid.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.livebidding.server.bid.exception.BidErrorCode;
import com.livebidding.server.bid.exception.BidException;
import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BidTest {

    private User bidder;
    private Product product;

    @BeforeEach
    void setUp() {
        bidder = mock(User.class);
        product = mock(Product.class);
    }

    @Test
    @DisplayName("유효한 정보로 Bid 객체를 성공적으로 생성한다.")
    void create_bid_successfully() {
        // when
        Bid bid = Bid.of(1000L, bidder, product);

        // then
        assertThat(bid).isNotNull();
        assertThat(bid.getPrice().getValue()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("입찰자(bidder)가 null일 경우 BidException을 던진다.")
    void throw_exception_when_bidder_is_null() {
        // when & then
        assertThatThrownBy(() -> Bid.of(1000L, null, product))
                .isInstanceOf(BidException.class)
                .hasFieldOrPropertyWithValue("errorCode", BidErrorCode.BIDDER_CANNOT_BE_NULL);
    }

    @Test
    @DisplayName("상품(product)이 null일 경우 BidException을 던진다.")
    void throw_exception_when_product_is_null() {
        // when & then
        assertThatThrownBy(() -> Bid.of(1000L, bidder, null))
                .isInstanceOf(BidException.class)
                .hasFieldOrPropertyWithValue("errorCode", BidErrorCode.PRODUCT_CANNOT_BE_NULL);
    }
}

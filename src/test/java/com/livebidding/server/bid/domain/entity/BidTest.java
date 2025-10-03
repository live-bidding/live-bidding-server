package com.livebidding.server.bid.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.product.domain.vo.Price;
import com.livebidding.server.support.TestFixture;
import com.livebidding.server.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BidTest {

    @Test
    @DisplayName("유효한 정보로 Bid 객체를 성공적으로 생성한다.")
    void createBidSuccess() {
        // given
        User seller = TestFixture.createUser("seller@test.com");
        User bidder = TestFixture.createUser("bidder@test.com");
        Product product = TestFixture.createProduct(seller);
        Long price = 150000L;

        // when
        Bid bid = Bid.of(price, bidder, product);

        // then
        assertThat(bid).isNotNull();
        assertThat(bid.getPrice()).isEqualTo(Price.from(price));
        assertThat(bid.getBidder()).isEqualTo(bidder);
        assertThat(bid.getProduct()).isEqualTo(product);
    }
}

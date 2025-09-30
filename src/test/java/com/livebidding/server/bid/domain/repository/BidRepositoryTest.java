package com.livebidding.server.bid.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.livebidding.server.bid.domain.entity.Bid;
import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.product.domain.repository.ProductRepository;
import com.livebidding.server.user.domain.entity.User;
import com.livebidding.server.user.domain.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class BidRepositoryTest {

    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Bid 엔티티를 성공적으로 저장한다.")
    void save_bid_successfully() {
        // given
        User seller = User.of("seller@test.com", "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy", "판매자");
        User bidder = User.of("bidder@test.com", "$2a$10$dRVc2G2Y32fW2./ssxGoKe0L9GkGu7C2s5GwAZxk5OB6KKAjB5wly", "입찰자");
        userRepository.save(seller);
        userRepository.save(bidder);

        Product product = Product.of("테스트 상품", "설명", 1000L,
                LocalDateTime.now(), LocalDateTime.now().plusDays(1), seller);
        productRepository.save(product);

        Bid bid = Bid.of(1500L, bidder, product);

        // when
        Bid savedBid = bidRepository.save(bid);

        // then
        assertThat(savedBid.getId()).isNotNull();
        assertThat(savedBid.getPrice().getValue()).isEqualTo(1500L);
        assertThat(savedBid.getBidder().getId()).isEqualTo(bidder.getId());
        assertThat(savedBid.getProduct().getId()).isEqualTo(product.getId());
        assertThat(savedBid.getBidTime()).isNotNull();
    }
}

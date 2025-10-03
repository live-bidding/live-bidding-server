package com.livebidding.server.bid.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.livebidding.server.bid.domain.entity.Bid;
import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.product.domain.repository.ProductRepository;
import com.livebidding.server.product.domain.vo.Price;
import com.livebidding.server.support.TestFixture;
import com.livebidding.server.user.domain.entity.User;
import com.livebidding.server.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BidRepositoryTest {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private User savedBidder;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        bidRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        User seller = userRepository.save(TestFixture.createUser("seller@test.com"));
        savedBidder = userRepository.save(TestFixture.createUser("bidder@test.com"));
        savedProduct = productRepository.save(TestFixture.createProduct(seller));
    }

    @Test
    @DisplayName("Bid 엔티티를 성공적으로 저장한다")
    void saveBidSuccess() {
        // given
        Bid bid = Bid.of(150000L, savedBidder, savedProduct);

        // when
        Bid savedBid = bidRepository.save(bid);

        // then
        assertThat(savedBid.getId()).isNotNull();
        assertThat(savedBid.getPrice()).isEqualTo(Price.from(150000L));
    }
}

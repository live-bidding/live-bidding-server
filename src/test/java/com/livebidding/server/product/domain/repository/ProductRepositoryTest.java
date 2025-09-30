package com.livebidding.server.product.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.user.domain.entity.User;
import com.livebidding.server.user.domain.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private User seller;

    @BeforeEach
    void setUp() {
        seller = User.of("seller@test.com", "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy", "판매자");
        userRepository.save(seller);
    }

    @Test
    @DisplayName("Product 엔티티를 성공적으로 저장한다.")
    void save_product_successfully() {
        // given
        Product product = Product.of("테스트 상품", "설명", 1000L,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), seller);

        // when
        Product savedProduct = productRepository.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName().getValue()).isEqualTo("테스트 상품");
        assertThat(savedProduct.getSeller().getId()).isEqualTo(seller.getId());
    }
}

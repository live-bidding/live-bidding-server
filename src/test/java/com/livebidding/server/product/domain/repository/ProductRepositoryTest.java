package com.livebidding.server.product.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.support.TestFixture;
import com.livebidding.server.user.domain.entity.User;
import com.livebidding.server.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // 외래키 관계 때문에 product 먼저 삭제
        productRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Product 엔티티를 성공적으로 저장한다.")
    void saveProductSuccess() {
        // given
        User seller = userRepository.save(TestFixture.createUser());
        Product product = TestFixture.createProduct(seller);

        // when
        Product savedProduct = productRepository.save(product);

        // then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getSeller().getId()).isEqualTo(seller.getId());
    }
}
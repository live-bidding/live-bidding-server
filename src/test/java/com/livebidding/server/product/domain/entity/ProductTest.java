package com.livebidding.server.product.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.livebidding.server.product.domain.type.ProductStatus;
import com.livebidding.server.user.domain.entity.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("유효한 정보로 Product 객체를 성공적으로 생성한다.")
    void create_product_successfully() {
        // given
        User seller = mock(User.class);
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusDays(2);

        // when
        Product product = Product.of("테스트 상품", "설명", 1000L, startTime, endTime, seller);

        // then
        assertThat(product).isNotNull();
        assertThat(product.getName().getValue()).isEqualTo("테스트 상품");
        assertThat(product.getStartPrice().getValue()).isEqualTo(1000L);
        assertThat(product.getCurrentPrice().getValue()).isEqualTo(1000L); // 현재가는 시작가와 동일
        assertThat(product.getStatus()).isEqualTo(ProductStatus.PREPARED); // 초기 상태는 PREPARED
        assertThat(product.getSeller()).isEqualTo(seller);
    }
}

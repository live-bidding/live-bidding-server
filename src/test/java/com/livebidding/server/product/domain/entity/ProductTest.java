package com.livebidding.server.product.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.livebidding.server.product.domain.type.ProductStatus;
import com.livebidding.server.product.exception.ProductErrorCode;
import com.livebidding.server.product.exception.ProductException;
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
        assertThat(product.getCurrentPrice().getValue()).isEqualTo(1000L);
        assertThat(product.getStatus()).isEqualTo(ProductStatus.PREPARED);
        assertThat(product.getSeller()).isEqualTo(seller);
    }

    @Test
    @DisplayName("판매자(seller)가 null일 경우 ProductException을 던진다.")
    void throw_exception_when_seller_is_null() {
        // given
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusDays(2);

        // when & then
        assertThatThrownBy(() -> Product.of("상품명", "설명", 1000L, startTime, endTime, null))
                .isInstanceOf(ProductException.class)
                .hasFieldOrPropertyWithValue("errorCode", ProductErrorCode.SELLER_CANNOT_BE_NULL);
    }
}

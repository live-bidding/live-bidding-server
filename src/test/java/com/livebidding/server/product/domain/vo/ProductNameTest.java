package com.livebidding.server.product.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.livebidding.server.product.exception.ProductErrorCode;
import com.livebidding.server.product.exception.ProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductNameTest {
    @Test
    @DisplayName("유효한 이름으로 ProductName 객체를 생성한다.")
    void create_productName_successfully() {
        // given
        String validName = "멋진 상품";

        // when
        ProductName name = ProductName.from(validName);

        // then
        assertThat(name).isNotNull();
        assertThat(name.getValue()).isEqualTo(validName);
    }

    @Test
    @DisplayName("이름이 비어있을 경우 ProductException을 던진다.")
    void throw_exception_when_name_is_empty() {
        // when & then
        assertThatThrownBy(() -> ProductName.from(""))
                .isInstanceOf(ProductException.class)
                .hasFieldOrPropertyWithValue("errorCode", ProductErrorCode.EMPTY_PRODUCT_NAME);
    }

    @Test
    @DisplayName("같은 값을 가진 ProductName 객체는 동등하다.")
    void objects_with_same_value_are_equal() {
        // given
        String nameValue = "동일한 상품명";
        ProductName name1 = ProductName.from(nameValue);
        ProductName name2 = ProductName.from(nameValue);

        // when & then
        assertThat(name1).isEqualTo(name2);
    }
}

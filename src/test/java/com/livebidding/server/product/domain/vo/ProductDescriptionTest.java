package com.livebidding.server.product.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.livebidding.server.product.exception.ProductErrorCode;
import com.livebidding.server.product.exception.ProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductDescriptionTest {
    @Test
    @DisplayName("유효한 설명으로 ProductDescription 객체를 생성한다.")
    void create_productDescription_successfully() {
        // given
        String validDescription = "이것은 상품 설명입니다.";

        // when
        ProductDescription description = ProductDescription.from(validDescription);

        // then
        assertThat(description).isNotNull();
        assertThat(description.getValue()).isEqualTo(validDescription);
    }

    @Test
    @DisplayName("설명이 비어있을 경우 ProductException을 던진다.")
    void throw_exception_when_description_is_empty() {
        // when & then
        assertThatThrownBy(() -> ProductDescription.from(""))
                .isInstanceOf(ProductException.class)
                .hasFieldOrPropertyWithValue("errorCode", ProductErrorCode.EMPTY_PRODUCT_DESCRIPTION);
    }

    @Test
    @DisplayName("같은 값을 가진 ProductDescription 객체는 동등하다.")
    void objects_with_same_value_are_equal() {
        // given
        String descriptionValue = "동일한 설명";
        ProductDescription description1 = ProductDescription.from(descriptionValue);
        ProductDescription description2 = ProductDescription.from(descriptionValue);

        // when & then
        assertThat(description1).isEqualTo(description2);
    }
}

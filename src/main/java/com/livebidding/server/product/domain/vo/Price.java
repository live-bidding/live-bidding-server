package com.livebidding.server.product.domain.vo;

import com.livebidding.server.product.exception.ProductErrorCode;
import com.livebidding.server.product.exception.ProductException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {

    private Long value;

    private Price(final Long value) {
        validate(value);
        this.value = value;
    }

    public static Price from(final Long value) {
        return new Price(value);
    }

    private void validate(final Long value) {
        if (value == null) {
            throw new ProductException(ProductErrorCode.PRICE_CANNOT_BE_NULL);
        }
        if (value < 0) {
            throw new ProductException(ProductErrorCode.PRICE_CANNOT_BE_NEGATIVE);
        }
    }
}

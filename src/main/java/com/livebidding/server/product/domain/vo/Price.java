package com.livebidding.server.product.domain.vo;

import com.livebidding.server.product.exception.ProductErrorCode;
import com.livebidding.server.product.exception.ProductException;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {

    private BigDecimal value;

    private Price(BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    public static Price from(Long value) {
        if (value == null) {
            throw new ProductException(ProductErrorCode.PRICE_CANNOT_BE_NULL);
        }
        return new Price(BigDecimal.valueOf(value));
    }

    public static Price from(BigDecimal value) {
        return new Price(value);
    }

    private void validatePrice(BigDecimal value) {
        if (value == null) {
            throw new ProductException(ProductErrorCode.PRICE_CANNOT_BE_NULL);
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductException(ProductErrorCode.PRICE_CANNOT_BE_NEGATIVE);
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}

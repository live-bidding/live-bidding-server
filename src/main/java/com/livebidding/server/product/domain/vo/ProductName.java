package com.livebidding.server.product.domain.vo;

import com.livebidding.server.product.exception.ProductErrorCode;
import com.livebidding.server.product.exception.ProductException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductName {

    @Column(name = "name", nullable = false)
    private String value;

    private ProductName(final String value) {
        validate(value);
        this.value = value;
    }

    public static ProductName from(final String value) {
        return new ProductName(value);
    }

    private void validate(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new ProductException(ProductErrorCode.EMPTY_PRODUCT_NAME);
        }
    }
}

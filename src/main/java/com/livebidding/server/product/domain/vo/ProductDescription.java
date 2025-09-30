package com.livebidding.server.product.domain.vo;

import com.livebidding.server.product.exception.ProductErrorCode;
import com.livebidding.server.product.exception.ProductException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDescription {

    @Lob
    @Column(name = "description", nullable = false)
    private String value;

    private ProductDescription(final String value) {
        validate(value);
        this.value = value;
    }

    public static ProductDescription from(final String value) {
        return new ProductDescription(value);
    }

    private void validate(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new ProductException(ProductErrorCode.EMPTY_PRODUCT_DESCRIPTION);
        }
    }
}

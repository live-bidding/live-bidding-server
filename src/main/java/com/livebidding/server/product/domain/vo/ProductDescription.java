package com.livebidding.server.product.domain.vo;

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

    /**
     * Creates a ProductDescription initialized with the given text after validating it.
     *
     * @param value the product description text
     * @throws IllegalArgumentException if {@code value} is null, empty, or contains only whitespace
     */
    private ProductDescription(final String value) {
        validate(value);
        this.value = value;
    }

    /**
     * Create a ProductDescription value object from the given text.
     *
     * @param value the product description text; must contain non-whitespace characters
     * @return a ProductDescription containing the provided text
     * @throws IllegalArgumentException if {@code value} is null, empty, or contains only whitespace
     */
    public static ProductDescription from(final String value) {
        return new ProductDescription(value);
    }

    /**
     * Validates that the product description contains non-whitespace text.
     *
     * @param value the product description to validate
     * @throws IllegalArgumentException if {@code value} is null, empty, or contains only whitespace
     */
    private void validate(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("[ERROR] 상품 설명은 공백일 수 없습니다.");
        }
    }
}

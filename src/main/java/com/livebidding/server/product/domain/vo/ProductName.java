package com.livebidding.server.product.domain.vo;

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

    /**
     * Creates a ProductName with the provided value after validating it is not blank.
     *
     * @param value the product name text
     * @throws IllegalArgumentException if value is null, empty, or contains only whitespace
     */
    private ProductName(final String value) {
        validate(value);
        this.value = value;
    }

    /**
     * Create a ProductName value object from the given string.
     *
     * @param value the product name text; must contain at least one non-whitespace character
     * @return a ProductName representing the given name
     * @throws IllegalArgumentException if {@code value} is null, empty, or contains only whitespace
     */
    public static ProductName from(final String value) {
        return new ProductName(value);
    }

    /**
     * Ensures the product name contains non-blank text.
     *
     * @param value the product name to validate
     * @throws IllegalArgumentException if `value` is null, empty, or contains only whitespace; message: "[ERROR] 상품명은 공백일 수 없습니다."
     */
    private void validate(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("[ERROR] 상품명은 공백일 수 없습니다.");
        }
    }
}

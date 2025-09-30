package com.livebidding.server.product.domain.vo;

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

    /**
     * Creates a Price value object after validating the supplied value.
     *
     * @param value the price amount; must be greater than or equal to 0
     * @throws IllegalArgumentException if {@code value} is {@code null} or less than 0
     */
    private Price(final Long value) {
        validate(value);
        this.value = value;
    }

    /**
     * Creates a Price value object for the given numeric value.
     *
     * @param value the price value; must be non-null and greater than or equal to 0
     * @return a new Price instance representing the given value
     * @throws IllegalArgumentException if {@code value} is {@code null} or less than 0
     */
    public static Price from(final Long value) {
        return new Price(value);
    }

    /**
     * Validates the provided price value.
     *
     * @param value the price to validate; must be non-null and greater than or equal to 0
     * @throws IllegalArgumentException if {@code value} is null or less than 0
     */
    private void validate(final Long value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("[ERROR] 가격은 0 이상이어야 합니다.");
        }
    }
}

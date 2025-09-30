package com.livebidding.server.product.domain.vo;

import jakarta.persistence.Column;
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

    @Column(name = "price")
    private Long value;

    private Price(final Long value) {
        validate(value);
        this.value = value;
    }

    public static Price from(final Long value) {
        return new Price(value);
    }

    private void validate(final Long value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("[ERROR] 가격은 0 이상이어야 합니다.");
        }
    }
}

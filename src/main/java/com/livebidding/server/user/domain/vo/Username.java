package com.livebidding.server.user.domain.vo;

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
public class Username {

    @Column(name = "name", nullable = false)
    private String value;

    /**
     * Creates a Username value object after verifying the provided name is not null, empty, or whitespace-only.
     *
     * @param value the username string; must contain at least one non-whitespace character
     */
    private Username(final String value) {
        validate(value);
        this.value = value;
    }

    /**
     * Creates a Username value object from the given string.
     *
     * @param value the username string; must contain at least one non-whitespace character
     * @return the created Username instance containing the validated username
     */
    public static Username from(final String value) {
        return new Username(value);
    }

    /**
     * Ensures the username is not null, empty, or only whitespace.
     *
     * @param value the username string to validate
     * @throws IllegalArgumentException if value is null, empty, or contains only whitespace
     */
    private void validate(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("[ERROR] 사용자 이름은 공백일 수 없습니다.");
        }
    }
}

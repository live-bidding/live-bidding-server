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
public class Password {

    private static final String BCRYPT_PREFIX = "$2a$";

    @Column(name = "password", nullable = false)
    private String value;

    /**
     * Creates a Password value object from an encoded password string.
     *
     * @param value the encoded password string; must contain text and start with the BCrypt prefix "$2a$"
     * @throws IllegalArgumentException if {@code value} is blank or does not start with "$2a$"
     */
    private Password(final String value) {
        validate(value);
        this.value = value;
    }

    /**
     * Creates a Password value object from a pre-encoded bcrypt string.
     *
     * @param encodedValue the bcrypt-encoded password string (must start with "$2a$")
     * @return the Password containing the provided encoded value
     * @throws IllegalArgumentException if encodedValue is null, blank, or does not start with "$2a$"
     */
    public static Password fromEncoded(final String encodedValue) {
        return new Password(encodedValue);
    }

    /**
     * Validates that the supplied password string is non-blank and uses the BCrypt format.
     *
     * @param value the encoded password string to validate
     * @throws IllegalArgumentException if `value` is null, empty, or contains only whitespace
     * @throws IllegalArgumentException if `value` does not start with the BCrypt prefix (`$2a$`)
     */
    private void validate(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("[ERROR] 비밀번호는 공백일 수 없습니다.");
        }
        if (!value.startsWith(BCRYPT_PREFIX)) {
            throw new IllegalArgumentException("[ERROR] 비밀번호가 암호화되지 않았습니다.");
        }
    }
}

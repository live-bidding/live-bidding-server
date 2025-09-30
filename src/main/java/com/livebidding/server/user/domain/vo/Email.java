package com.livebidding.server.user.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Column(name = "email", nullable = false, unique = true)
    private String value;

    /**
     * Creates a new Email value object after validating the provided address.
     *
     * @param value the email address to validate and store
     * @throws IllegalArgumentException if the value is null or does not match the expected email format
     */
    private Email(final String value) {
        validate(value);
        this.value = value;
    }

    /**
     * Creates an Email value object from the given string.
     *
     * @param value the email address to wrap; must be non-null and match the required email format
     * @return an Email instance representing the provided address
     * @throws IllegalArgumentException if the value is null or does not match the email format
     */
    public static Email from(final String value) {
        return new Email(value);
    }

    /**
     * Validates that the provided email string is not null and conforms to the configured email pattern.
     *
     * @param value the email string to validate
     * @throws IllegalArgumentException if {@code value} is null or does not match the email format
     */
    private void validate(final String value) {
        if (value == null || !EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 이메일 형식입니다.");
        }
    }
}

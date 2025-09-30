package com.livebidding.server.user.domain.vo;

import com.livebidding.server.user.exception.UserErrorCode;
import com.livebidding.server.user.exception.UserException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

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

    private Email(final String value) {
        String normalizedEmail = normalize(value);
        validateFormat(normalizedEmail);
        this.value = normalizedEmail;
    }

    public static Email from(final String value) {
        return new Email(value);
    }

    private String normalize(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new UserException(UserErrorCode.INVALID_EMAIL_FORMAT);
        }
        return value.trim().toLowerCase();
    }

    private void validateFormat(final String value) {
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new UserException(UserErrorCode.INVALID_EMAIL_FORMAT);
        }
    }
}
package com.livebidding.server.auth.domain.vo;

import com.livebidding.server.auth.exception.AuthErrorCode;
import com.livebidding.server.auth.exception.AuthException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class TokenValue {

    @Column(name = "token_value", nullable = false, unique = true, length = 500)
    private String value;

    private TokenValue(final String value) {
        validate(value);
        this.value = value;
    }

    public static TokenValue from(final String value) {
        return new TokenValue(value);
    }

    private void validate(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new AuthException(AuthErrorCode.INVALID_JWT_TOKEN);
        }
    }
}

package com.livebidding.server.auth.domain.vo;

import com.livebidding.server.auth.exception.AuthErrorCode;
import com.livebidding.server.auth.exception.AuthException;
import com.livebidding.server.global.error.BaseException;
import com.livebidding.server.global.error.GlobalErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
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

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int TOKEN_VALUE_MAX_LENGTH = 64;

    @Column(name = "token_value", nullable = false, length = TOKEN_VALUE_MAX_LENGTH)
    private String value;

    private TokenValue(final String value) {
        validate(value);
        this.value = value;
    }

    public static TokenValue from(final String value) {
        return new TokenValue(value);
    }

    public static TokenValue hashed(final String rawToken) {
        if (!StringUtils.hasText(rawToken)) {
            throw new AuthException(AuthErrorCode.INVALID_JWT_TOKEN);
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            String hexHash = HexFormat.of().formatHex(hash);
            return new TokenValue(hexHash);
        } catch (NoSuchAlgorithmException e) {
            throw new AuthException(GlobalErrorCode.UNSUPPORTED_HASH_ALGORITHM);
        }
    }

    private void validate(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new AuthException(AuthErrorCode.INVALID_JWT_TOKEN);
        }
    }
}

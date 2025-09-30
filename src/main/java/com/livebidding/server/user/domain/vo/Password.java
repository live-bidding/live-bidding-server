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

    private static final String BCRYPT_ID_PREFIX = "{bcrypt}";

    @Column(name = "password", nullable = false)
    private String value;

    private Password(final String value) {
        validate(value);
        this.value = value;
    }

    public static Password fromEncoded(final String encodedValue) {
        return new Password(encodedValue);
    }

    private void validate(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("[ERROR] 비밀번호는 공백일 수 없습니다.");
        }

        String hash = value;
        if (hash.startsWith(BCRYPT_ID_PREFIX)) {
            hash = hash.substring(BCRYPT_ID_PREFIX.length());
        }

        if (!hash.startsWith("$2a$") && !hash.startsWith("$2b$") && !hash.startsWith("$2y$")) {
            throw new IllegalArgumentException("[ERROR] 비밀번호가 BCrypt 형식으로 암호화되지 않았습니다.");
        }
    }
}

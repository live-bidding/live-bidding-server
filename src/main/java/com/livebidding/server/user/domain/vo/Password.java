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
        if (!value.startsWith(BCRYPT_PREFIX)) {
            throw new IllegalArgumentException("[ERROR] 비밀번호가 암호화되지 않았습니다.");
        }
    }
}

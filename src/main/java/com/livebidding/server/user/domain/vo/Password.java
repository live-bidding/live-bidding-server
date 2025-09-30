package com.livebidding.server.user.domain.vo;

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
public class Password {

    private static final Pattern BCRYPT_PATTERN =
            Pattern.compile("^(\\{bcrypt\\})?\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");

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
        if (!BCRYPT_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("[ERROR] 비밀번호가 유효한 BCrypt 형식이 아닙니다.");
        }
    }
}

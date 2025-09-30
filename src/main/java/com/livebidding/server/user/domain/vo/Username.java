package com.livebidding.server.user.domain.vo;

import com.livebidding.server.user.exception.UserErrorCode;
import com.livebidding.server.user.exception.UserException;
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

    private Username(final String value) {
        validate(value);
        this.value = value;
    }

    public static Username from(final String value) {
        return new Username(value);
    }

    private void validate(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new UserException(UserErrorCode.EMPTY_USERNAME);
        }
    }
}

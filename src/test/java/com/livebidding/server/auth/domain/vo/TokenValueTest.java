package com.livebidding.server.auth.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.livebidding.server.auth.exception.AuthErrorCode;
import com.livebidding.server.auth.exception.AuthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TokenValueTest {

    @Test
    @DisplayName("유효한 토큰 값으로 TokenValue 객체를 생성한다")
    void create_tokenValue_successfully() {
        // given
        String validToken = "a.b.c";

        // when
        TokenValue tokenValue = TokenValue.from(validToken);

        // then
        assertThat(tokenValue).isNotNull();
        assertThat(tokenValue.getValue()).isEqualTo(validToken);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  "})
    @DisplayName("토큰 값이 비어있거나 공백이면 예외를 던진다")
    void throw_exception_when_value_is_blank(String blankToken) {
        // when & then
        assertThatThrownBy(() -> TokenValue.from(blankToken))
            .isInstanceOf(AuthException.class)
            .hasFieldOrPropertyWithValue("errorCode", AuthErrorCode.INVALID_JWT_TOKEN);
    }

    @Test
    @DisplayName("토큰 값이 null이면 예외를 던진다")
    void throw_exception_when_value_is_null() {
        // when & then
        assertThatThrownBy(() -> TokenValue.from(null))
            .isInstanceOf(AuthException.class)
            .hasFieldOrPropertyWithValue("errorCode", AuthErrorCode.INVALID_JWT_TOKEN);
    }

    @Test
    @DisplayName("같은 값을 가진 TokenValue 객체는 동등하다")
    void objects_with_same_value_are_equal() {
        // given
        String token = "a.b.c";
        TokenValue tokenValue1 = TokenValue.from(token);
        TokenValue tokenValue2 = TokenValue.from(token);

        // when & then
        assertThat(tokenValue1).isEqualTo(tokenValue2);
        assertThat(tokenValue1.hashCode()).isEqualTo(tokenValue2.hashCode());
    }
}

package com.livebidding.server.user.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordTest {

    @Test
    @DisplayName("유효한 암호화된 문자열로 Password 객체를 생성한다.")
    void create_password_with_encoded_value() {
        // given
        String encodedPassword = "$2a$10$abcdefghijklmnopqrstuvwxyz123456";

        // when
        Password password = Password.fromEncoded(encodedPassword);

        // then
        assertThat(password).isNotNull();
        assertThat(password.getValue()).isEqualTo(encodedPassword);
    }

    @Test
    @DisplayName("암호화되지 않은 평문 문자열일 경우 예외를 던진다.")
    void throw_exception_for_plain_text() {
        // given
        String plainPassword = "password123";

        // when & then
        assertThatThrownBy(() -> Password.fromEncoded(plainPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 비밀번호가 암호화되지 않았습니다.");
    }

    @Test
    @DisplayName("비어있는 문자열일 경우 예외를 던진다.")
    void throw_exception_for_empty_string() {
        // when & then
        assertThatThrownBy(() -> Password.fromEncoded(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 비밀번호는 공백일 수 없습니다.");
    }
}

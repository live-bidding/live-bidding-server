package com.livebidding.server.user.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "$2a$10$abcdefghijklmnopqrstuvwxyz123456",
            "$2b$10$abcdefghijklmnopqrstuvwxyz123456",
            "$2y$10$abcdefghijklmnopqrstuvwxyz123456",
            "{bcrypt}$2a$10$abcdefghijklmnopqrstuvwxyz123456"
    })
    @DisplayName("다양한 BCrypt 형식의 암호화된 문자열로 Password 객체를 생성한다.")
    void create_password_with_various_encoded_formats(String encodedPassword) {
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
                .hasMessageContaining("[ERROR] 비밀번호가 BCrypt 형식으로 암호화되지 않았습니다.");
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

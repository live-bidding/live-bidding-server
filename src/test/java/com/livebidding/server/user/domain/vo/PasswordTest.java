package com.livebidding.server.user.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.livebidding.server.user.exception.UserErrorCode;
import com.livebidding.server.user.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy",
            "$2b$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy",
            "$2y$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy",
            "{bcrypt}$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"
    })
    @DisplayName("유효한 전체 BCrypt 형식의 문자열로 Password 객체를 생성한다.")
    void create_password_with_valid_full_bcrypt_hash(String encodedPassword) {
        // when
        Password password = Password.fromEncoded(encodedPassword);

        // then
        assertThat(password).isNotNull();
        assertThat(password.getValue()).isEqualTo(encodedPassword);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "password123", // 평문
            "$2a$10$short",  // 짧은 해시
            "$2a$10$abcdefghijklmnopqrstuvwxyz123456" // 잘못된 길이의 해시
    })
    @DisplayName("유효하지 않은 형식의 문자열일 경우 UserException을 던진다.")
    void throw_exception_for_invalid_format(String invalidPassword) {
        // when & then
        assertThatThrownBy(() -> Password.fromEncoded(invalidPassword))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.INVALID_PASSWORD_FORMAT);
    }

    @Test
    @DisplayName("비어있는 문자열일 경우 UserException을 던진다.")
    void throw_exception_for_empty_string() {
        // when & then
        assertThatThrownBy(() -> Password.fromEncoded(""))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.EMPTY_PASSWORD);
    }
}

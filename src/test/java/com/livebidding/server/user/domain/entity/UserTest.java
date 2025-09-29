package com.livebidding.server.user.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("유효한 정보로 User 객체를 성공적으로 생성한다.")
    void create_user_successfully() {
        // given
        String email = "test@example.com";
        String encodedPassword = "$2a$10$abcdefghijklmnopqrstuvwxyz123456";
        String name = "테스터";

        // when
        User user = User.of(email, encodedPassword, name);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getEmail().getValue()).isEqualTo(email);
        assertThat(user.getPassword().getValue()).isEqualTo(encodedPassword);
        assertThat(user.getName().getValue()).isEqualTo(name);
    }

    @Test
    @DisplayName("유효하지 않은 이메일로 User 객체 생성 시 예외를 던진다.")
    void create_user_with_invalid_email_throws_exception() {
        // given
        String invalidEmail = "invalid-email";
        String encodedPassword = "$2a$10$abcdefghijklmnopqrstuvwxyz123456";
        String name = "테스터";

        // when & then
        assertThatThrownBy(() -> User.of(invalidEmail, encodedPassword, name))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

package com.livebidding.server.user.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.livebidding.server.user.exception.UserErrorCode;
import com.livebidding.server.user.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsernameTest {

    @Test
    @DisplayName("유효한 이름으로 Username 객체를 생성한다.")
    void create_username_with_valid_name() {
        // given
        String validName = "홍길동";

        // when
        Username username = Username.from(validName);

        // then
        assertThat(username).isNotNull();
        assertThat(username.getValue()).isEqualTo(validName);
    }

    @Test
    @DisplayName("비어있는 문자열일 경우 UserException을 던진다.")
    void throw_exception_for_empty_string() {
        // when & then
        assertThatThrownBy(() -> Username.from(""))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.EMPTY_USERNAME);
    }
}

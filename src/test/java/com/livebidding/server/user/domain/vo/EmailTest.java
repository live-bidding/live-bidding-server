package com.livebidding.server.user.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.livebidding.server.user.exception.UserErrorCode;
import com.livebidding.server.user.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

    @Test
    @DisplayName("유효한 이메일 형식으로 Email 객체를 생성한다.")
    void create_email_with_valid_format() {
        // given
        String validEmail = "test@example.com";

        // when
        Email email = Email.from(validEmail);

        // then
        assertThat(email).isNotNull();
        assertThat(email.getValue()).isEqualTo(validEmail);
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "test@", "@example.com", "test@.com"})
    @DisplayName("유효하지 않은 이메일 형식일 경우 UserException을 던진다.")
    void throw_exception_for_invalid_format(String invalidEmail) {
        // when & then
        assertThatThrownBy(() -> Email.from(invalidEmail))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.INVALID_EMAIL_FORMAT);
    }

    @Test
    @DisplayName("같은 값을 가진 Email 객체는 동등하다.")
    void email_objects_with_same_value_are_equal() {
        // given
        String emailValue = "equal@example.com";
        Email email1 = Email.from(emailValue);
        Email email2 = Email.from(emailValue);

        // when & then
        assertThat(email1).isEqualTo(email2);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
    }
}

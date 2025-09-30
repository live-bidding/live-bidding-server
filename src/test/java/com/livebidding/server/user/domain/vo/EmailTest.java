package com.livebidding.server.user.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.livebidding.server.user.exception.UserErrorCode;
import com.livebidding.server.user.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

	@DisplayName("유효한 형식의 이메일로 객체를 생성한다.")
	@Test
	void createEmail() {
		// given
		String value = "test@test.com";

		// when
		Email email = Email.from(value);

		// then
		assertThat(email.getValue()).isEqualTo(value);
	}

	@DisplayName("유효하지 않은 형식의 이메일로 객체를 생성하면 예외가 발생한다.")
	@ParameterizedTest
	@ValueSource(strings = {"invalid-email", "test@", "@test.com"})
	void createEmailWithInvalidFormat(String invalidEmail) {
		assertThatThrownBy(() -> Email.from(invalidEmail))
				.isInstanceOf(UserException.class)
				.extracting("errorCode")
				.isEqualTo(UserErrorCode.INVALID_EMAIL_FORMAT);
	}

	@DisplayName("null, 비어있거나 공백으로만 이루어진 이메일로 객체를 생성하면 예외가 발생한다.")
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" ", "   "})
	void createEmailWithBlankValue(String blankEmail) {
		assertThatThrownBy(() -> Email.from(blankEmail))
				.isInstanceOf(UserException.class)
				.extracting("errorCode")
				.isEqualTo(UserErrorCode.INVALID_EMAIL_FORMAT);
	}

	@DisplayName("이메일은 정규화(trim, 소문자)되어 저장된다.")
	@Test
	void createEmailWithNormalization() {
		// given
		String originalEmail = "  Test@Test.Com  ";
		String normalizedEmail = "test@test.com";

		// when
		Email email = Email.from(originalEmail);

		// then
		assertThat(email.getValue()).isEqualTo(normalizedEmail);
	}
}
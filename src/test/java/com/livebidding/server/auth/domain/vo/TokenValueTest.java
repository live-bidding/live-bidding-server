package com.livebidding.server.auth.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.livebidding.server.auth.exception.AuthErrorCode;
import com.livebidding.server.auth.exception.AuthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class TokenValueTest {

	@DisplayName("raw 토큰을 해싱하여 TokenValue 객체를 생성한다.")
	@Test
	void createHashedTokenValue() {
		// given
		String rawToken = "test-token-1234";

		// when
		TokenValue tokenValue = TokenValue.hashed(rawToken);

		// then
		assertThat(tokenValue).isNotNull();
		assertThat(tokenValue.getValue()).isNotEqualTo(rawToken);
		assertThat(tokenValue.getValue()).hasSize(64); // SHA-256 hex string length
	}

	@DisplayName("null, 비어있거나 공백으로만 이루어진 raw 토큰으로 해싱하면 예외가 발생한다.")
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" ", "   "})
	void createHashedTokenWithBlankValue(String blankToken) {
		// when & then
		assertThatThrownBy(() -> TokenValue.hashed(blankToken))
				.isInstanceOf(AuthException.class)
				.extracting("errorCode")
				.isEqualTo(AuthErrorCode.INVALID_JWT_TOKEN);
	}

	@DisplayName("일반 문자열로 TokenValue 객체를 생성한다.")
	@Test
	void createTokenValueFromPlainString() {
		// given
		String value = "some-plain-value";

		// when
		TokenValue tokenValue = TokenValue.from(value);

		// then
		assertThat(tokenValue.getValue()).isEqualTo(value);
	}

}
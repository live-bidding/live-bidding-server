package com.livebidding.server.auth.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.livebidding.server.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RefreshTokenTest {

    @Test
    @DisplayName("of 팩토리 메소드로 RefreshToken 객체를 생성한다")
    void create_refreshToken_successfully() {
        // given
        User user = mock(User.class);
        String tokenValue = "a.b.c";

        // when
        RefreshToken refreshToken = RefreshToken.of(user, tokenValue);

        // then
        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.getUser()).isEqualTo(user);
        assertThat(refreshToken.getTokenValue().getValue()).isEqualTo(tokenValue);
    }

    @Test
    @DisplayName("updateToken 메소드로 토큰 값을 변경한다")
    void update_token_successfully() {
        // given
        User user = mock(User.class);
        RefreshToken refreshToken = RefreshToken.of(user, "old.token.value");
        String newTokenValue = "new.token.value";

        // when
        refreshToken.updateToken(newTokenValue);

        // then
        assertThat(refreshToken.getTokenValue().getValue()).isEqualTo(newTokenValue);
    }
}

package com.livebidding.server.auth.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.livebidding.server.auth.domain.entity.RefreshToken;
import com.livebidding.server.user.domain.entity.User;
import com.livebidding.server.user.domain.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 ID로 RefreshToken을 조회한다")
    void find_by_user_id() {
        // given
        User user = User.of("test@test.com", "$2a$10$N9qo8uLOickgx2ZMRZoMye.aA.w.dTBxI1s/oSp5kL182bGRfB0Fi", "testuser");
        userRepository.save(user);

        String tokenValue = "a.b.c";
        RefreshToken refreshToken = RefreshToken.of(user, tokenValue);
        refreshTokenRepository.save(refreshToken);

        // when
        Optional<RefreshToken> foundToken = refreshTokenRepository.findByUserId(user.getId());

        // then
        assertThat(foundToken).isPresent();
        assertThat(foundToken.get().getUser()).isEqualTo(user);
        assertThat(foundToken.get().getTokenValue().getValue()).isEqualTo(tokenValue);
    }
}

package com.livebidding.server.user.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.livebidding.server.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final String validEncodedPassword1 = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
    private final String validEncodedPassword2 = "$2a$10$dRVc2G2Y32fW2./ssxGoKe0L9GkGu7C2s5GwAZxk5OB6KKAjB5wly";

    @Test
    @DisplayName("User 엔티티를 성공적으로 저장한다.")
    void save_user_successfully() {
        // given
        User user = User.of("test@example.com", validEncodedPassword1, "테스터");

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail().getValue()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("중복된 이메일로 저장 시 예외가 발생한다.")
    void throw_exception_when_saving_duplicate_email() {
        // given
        User user1 = User.of("duplicate@example.com", validEncodedPassword1, "사용자1");
        User user2 = User.of("duplicate@example.com", validEncodedPassword2, "사용자2");
        userRepository.save(user1);

        // when & then
        assertThatThrownBy(() -> userRepository.saveAndFlush(user2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

package com.livebidding.server.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.livebidding.server.user.domain.entity.User;
import com.livebidding.server.user.domain.repository.UserRepository;
import com.livebidding.server.user.domain.vo.Email;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 이름(이메일)으로 UserDetails를 성공적으로 조회한다")
    void loadUserByUsername_success() {
        // given
        String email = "test@test.com";
        User user = User.of(email, "$2a$10$N9qo8uLOickgx2ZMRZoMye.aA.w.dTBxI1s/oSp5kL182bGRfB0Fi", "testuser");
        given(userRepository.findByEmail(any(Email.class))).willReturn(Optional.of(user));

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(email);
    }

    @Test
    @DisplayName("사용자를 찾지 못하면 UsernameNotFoundException을 던진다")
    void loadUserByUsername_fail_userNotFound() {
        // given
        String email = "notfound@test.com";
        given(userRepository.findByEmail(any(Email.class))).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}

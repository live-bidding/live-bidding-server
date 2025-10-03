package com.livebidding.server.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.livebidding.server.auth.domain.entity.RefreshToken;
import com.livebidding.server.auth.domain.repository.RefreshTokenRepository;
import com.livebidding.server.auth.jwt.JwtTokenProvider;
import com.livebidding.server.user.api.dto.request.LoginRequest;
import com.livebidding.server.user.api.dto.request.SignupRequest;
import com.livebidding.server.user.api.dto.response.TokenResponse;
import com.livebidding.server.user.domain.entity.User;
import com.livebidding.server.user.domain.repository.UserRepository;
import com.livebidding.server.user.domain.vo.Email;
import com.livebidding.server.user.exception.UserErrorCode;
import com.livebidding.server.user.exception.UserException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationProvider authenticationProvider;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Nested
    @DisplayName("회원가입 시")
    class Signup {
        @Test
        @DisplayName("성공한다")
        void signup_success() {
            // given
            SignupRequest request = new SignupRequest("test@test.com", "password123", "testuser");
            given(passwordEncoder.encode(request.password())).willReturn("$2a$10$N9qo8uLOickgx2ZMRZoMye.aA.w.dTBxI1s/oSp5kL182bGRfB0Fi");

            // when
            userService.signup(request);

            // then
            verify(userRepository).saveAndFlush(any(User.class));
        }

        @Test
        @DisplayName("중복된 이메일이면 예외를 던진다")
        void signup_fail_duplicate_email() {
            // given
            SignupRequest request = new SignupRequest("test@test.com", "password123", "testuser");
            given(passwordEncoder.encode(request.password())).willReturn("$2a$10$N9qo8uLOickgx2ZMRZoMye.aA.w.dTBxI1s/oSp5kL182bGRfB0Fi");
            given(userRepository.saveAndFlush(any(User.class)))
                    .willThrow(new DataIntegrityViolationException("Duplicate entry 'test@test.com' for key 'email'"));

            // when & then
            assertThatThrownBy(() -> userService.signup(request))
                    .isInstanceOf(UserException.class)
                    .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Nested
    @DisplayName("로그인 시")
    class Login {
        @Test
        @DisplayName("성공하면 토큰을 반환한다")
        void login_success() {
            // given
            LoginRequest request = new LoginRequest("test@test.com", "password123");
            Authentication authentication = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            User user = User.of(request.email(), "$2a$10$N9qo8uLOickgx2ZMRZoMye.aA.w.dTBxI1s/oSp5kL182bGRfB0Fi", "testuser");

            given(authenticationProvider.authenticate(any())).willReturn(authentication);
            given(userRepository.findByEmail(any(Email.class))).willReturn(Optional.of(user));
            given(jwtTokenProvider.createAccessToken(authentication)).willReturn("accessToken");
            given(jwtTokenProvider.createRefreshToken(authentication)).willReturn("refreshToken");
            given(refreshTokenRepository.findByUserId(any())).willReturn(Optional.empty());

            // when
            TokenResponse response = userService.login(request);

            // then
            assertThat(response.accessToken()).isEqualTo("accessToken");
            assertThat(response.refreshToken()).isEqualTo("refreshToken");
            verify(refreshTokenRepository).save(any(RefreshToken.class));
        }

        @Test
        @DisplayName("자격 증명이 올바르지 않으면 예외를 던진다")
        void login_fail_wrong_password() {
            // given
            LoginRequest request = new LoginRequest("test@test.com", "wrongPassword");
            given(authenticationProvider.authenticate(any())).willThrow(new BadCredentialsException("Wrong password"));

            // when & then
            assertThatThrownBy(() -> userService.login(request))
                    .isInstanceOf(UserException.class)
                    .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.INVALID_CREDENTIALS);
        }
    }
}

package com.livebidding.server.user.application;

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
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationProvider authenticationProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponse login(LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.email(), request.password());
            Authentication authentication = authenticationProvider.authenticate(authenticationToken);

            User user = userRepository.findByEmail(Email.from(request.email()))
                    .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

            String accessToken = jwtTokenProvider.createAccessToken(authentication);
            String refreshTokenValue = jwtTokenProvider.createRefreshToken(authentication);

            updateOrCreateRefreshToken(user, refreshTokenValue);

            return new TokenResponse(accessToken, refreshTokenValue);
        } catch (AuthenticationException e) {
            throw new UserException(UserErrorCode.INVALID_CREDENTIALS);
        }
    }

    private void updateOrCreateRefreshToken(User user, String refreshTokenValue) {
        refreshTokenRepository.findByUserId(user.getId())
                .ifPresentOrElse(
                        refreshToken -> refreshToken.updateToken(refreshTokenValue),
                        () -> refreshTokenRepository.save(RefreshToken.of(user, refreshTokenValue))
                );
    }

    @Transactional
    public void signup(SignupRequest request) {
        try {
            String encodedPassword = passwordEncoder.encode(request.password());
            User user = User.of(request.email(), encodedPassword, request.name());
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL, e);
        }
    }
}

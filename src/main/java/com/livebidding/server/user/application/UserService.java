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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponse login(LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.email(), request.password());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            User user = userRepository.findByEmail(Email.from(request.email()))
                    .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

            String accessToken = jwtTokenProvider.createAccessToken(authentication);
            String refreshTokenValue = jwtTokenProvider.createRefreshToken(authentication);

            refreshTokenRepository.findByUserId(user.getId())
                    .ifPresentOrElse(
                            refreshToken -> refreshToken.updateToken(refreshTokenValue),
                            () -> refreshTokenRepository.save(RefreshToken.of(user, refreshTokenValue))
                    );

            return new TokenResponse(accessToken, refreshTokenValue);
        } catch (BadCredentialsException e) {
            throw new UserException(UserErrorCode.WRONG_PASSWORD);
        }
    }

    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(Email.from(request.email()))) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.of(request.email(), encodedPassword, request.name());
        userRepository.save(user);
    }
}

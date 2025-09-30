package com.livebidding.server.auth.domain.entity;

import com.livebidding.server.auth.domain.vo.TokenValue;
import com.livebidding.server.user.domain.entity.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Embedded
    private TokenValue tokenValue;

    private RefreshToken(User user, TokenValue tokenValue) {
        this.user = user;
        this.tokenValue = tokenValue;
    }

    public static RefreshToken of(User user, String tokenValue) {
        return new RefreshToken(user, TokenValue.from(tokenValue));
    }

    public void updateToken(String newTokenValue) {
        this.tokenValue = TokenValue.from(newTokenValue);
    }
}
package com.livebidding.server.auth.jwt;

import com.livebidding.server.auth.exception.AuthErrorCode;
import com.livebidding.server.auth.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expire-time}")
    private long accessTokenExpireTime;

    @Value("${jwt.refresh-token-expire-time}")
    private long refreshTokenExpireTime;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createAccessToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpireTime);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpireTime);

        return Jwts.builder()
            .setSubject(authentication.getName())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new AuthException(AuthErrorCode.INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AuthErrorCode.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new AuthException(AuthErrorCode.UNSUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new AuthException(AuthErrorCode.INVALID_JWT_TOKEN);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        User principal = new User(claims.getSubject(), "", Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(principal, token, Collections.emptyList());
    }
}
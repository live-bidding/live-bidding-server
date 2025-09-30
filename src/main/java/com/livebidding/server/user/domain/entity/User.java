package com.livebidding.server.user.domain.entity;

import com.livebidding.server.user.domain.vo.Email;
import com.livebidding.server.user.domain.vo.Password;
import com.livebidding.server.user.domain.vo.Username;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private Username name;

    /**
     * Creates a new User instance with the provided value objects.
     *
     * @param email   the Email value object for the user's email address
     * @param password the Password value object for the user's encoded password
     * @param name    the Username value object for the user's display name
     */
    private User(final Email email, final Password password, final Username name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    /**
     * Create a new User from an email address, an already-encoded password, and a username.
     *
     * @param email the user's email address
     * @param encodedPassword the user's password that has already been encoded
     * @param name the user's display name
     * @return a new User initialized with the given email, encoded password, and username
     */
    public static User of(final String email, final String encodedPassword, final String name) {
        return new User(
                Email.from(email),
                Password.fromEncoded(encodedPassword),
                Username.from(name)
        );
    }
}

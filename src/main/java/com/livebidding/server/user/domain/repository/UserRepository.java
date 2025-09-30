package com.livebidding.server.user.domain.repository;

import com.livebidding.server.user.domain.entity.User;
import com.livebidding.server.user.domain.vo.Email;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(Email email);

    Optional<User> findByEmail(Email email);
}
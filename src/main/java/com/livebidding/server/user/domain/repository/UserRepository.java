package com.livebidding.server.user.domain.repository;

import com.livebidding.server.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

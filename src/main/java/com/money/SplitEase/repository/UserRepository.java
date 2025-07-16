package com.money.SplitEase.repository;

import com.money.SplitEase.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Find a user by either username or email (commonly used in login forms).
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * Check if a user with email exists.
     */
    boolean existsByEmail(String email);

    /**
     * Check if a user with username exists.
     */
    boolean existsByUsername(String username);
}

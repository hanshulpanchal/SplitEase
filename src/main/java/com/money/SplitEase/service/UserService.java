package com.money.SplitEase.service;

import com.money.SplitEase.model.User;
import com.money.SplitEase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Create a new user.
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Get a user by ID.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Get all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Update user details if the user exists.
     */
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existing -> {
            existing.setUsername(updatedUser.getUsername());
            existing.setEmail(updatedUser.getEmail());
            existing.setPassword(updatedUser.getPassword());
            existing.setGroups(updatedUser.getGroups());
            return userRepository.save(existing);
        });
    }

    /**
     * Delete a user by ID if present.
     */
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Get a user by username.
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Get a user by email.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Check if a user exists by email.
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Check if a user exists by username.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}

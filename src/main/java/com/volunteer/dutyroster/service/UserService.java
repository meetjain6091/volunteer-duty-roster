package com.volunteer.dutyroster.service;

import com.volunteer.dutyroster.model.User;
import com.volunteer.dutyroster.model.UserRole;
import com.volunteer.dutyroster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing User operations.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a new user with encoded password.
     */
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Update an existing user. Only encodes password if it was changed.
     */
    public User updateUser(Long id, User updatedUser) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existing.setFullName(updatedUser.getFullName());
        existing.setEmail(updatedUser.getEmail());
        existing.setPhone(updatedUser.getPhone());
        existing.setRole(updatedUser.getRole());
        existing.setActive(updatedUser.isActive());

        // Only update password if a new one is provided
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existing);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public List<User> findActiveVolunteers() {
        return userRepository.findByRoleAndActiveTrue(UserRole.VOLUNTEER);
    }

    public List<User> searchByName(String name) {
        return userRepository.findByFullNameContainingIgnoreCase(name);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public long countAll() {
        return userRepository.count();
    }

    public long countByRole(UserRole role) {
        return userRepository.findByRole(role).size();
    }
}

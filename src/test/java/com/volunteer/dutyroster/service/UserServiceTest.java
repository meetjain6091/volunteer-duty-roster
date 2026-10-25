package com.volunteer.dutyroster.service;

import com.volunteer.dutyroster.model.User;
import com.volunteer.dutyroster.model.UserRole;
import com.volunteer.dutyroster.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User("meetjain", "rawPassword", "Meet Jain", "meet@example.com", UserRole.VOLUNTEER);
    }

    @Test
    @DisplayName("Create user encodes password before saving")
    void testCreateUserEncodesPassword() {
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User created = userService.createUser(sampleUser);

        assertNotNull(created);
        assertEquals("encodedPassword", created.getPassword());
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(sampleUser);
    }

    @Test
    @DisplayName("Check if username already exists")
    void testUsernameExists() {
        when(userRepository.existsByUsername("meetjain")).thenReturn(true);
        when(userRepository.existsByUsername("nonexistent")).thenReturn(false);

        assertTrue(userService.usernameExists("meetjain"));
        assertFalse(userService.usernameExists("nonexistent"));
    }

    @Test
    @DisplayName("Find active volunteers returns filtered list")
    void testFindActiveVolunteers() {
        when(userRepository.findByRoleAndActiveTrue(UserRole.VOLUNTEER)).thenReturn(List.of(sampleUser));

        List<User> volunteers = userService.findActiveVolunteers();

        assertEquals(1, volunteers.size());
        assertEquals("Meet Jain", volunteers.get(0).getFullName());
    }

    @Test
    @DisplayName("Update user without changing password preserves existing password")
    void testUpdateUserPreservesPasswordWhenNotProvided() {
        User existing = new User("meetjain", "existingHash", "Old Name", "old@example.com", UserRole.VOLUNTEER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User updatedInfo = new User("meetjain", "", "New Name", "new@example.com", UserRole.COORDINATOR);
        User result = userService.updateUser(1L, updatedInfo);

        assertEquals("New Name", result.getFullName());
        assertEquals("existingHash", result.getPassword());
        verify(passwordEncoder, never()).encode(any());
    }
}

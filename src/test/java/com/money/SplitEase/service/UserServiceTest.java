package com.money.SplitEase.service;

import com.money.SplitEase.model.User;
import com.money.SplitEase.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // ✅ Use this instead of @SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .username("john")
                .email("john@example.com")
                .password("secret123")
                .groups(Collections.emptySet())
                .build();
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        User created = userService.createUser(sampleUser);
        assertNotNull(created);
        assertEquals("john", created.getUsername());
    }

    @Test
    void testGetUserByIdFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        Optional<User> found = userService.getUserById(1L);
        assertTrue(found.isPresent());
        assertEquals("john", found.get().getUsername());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<User> found = userService.getUserById(2L);
        assertTrue(found.isEmpty());
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));

        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("john", users.get(0).getUsername());
    }

    @Test
    void testUpdateUserWhenExists() {
        User updatedUser = User.builder()
                .id(1L)
                .username("johnny")
                .email("johnny@example.com")
                .password("newpass")
                .groups(Collections.emptySet())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        Optional<User> result = userService.updateUser(1L, updatedUser);
        assertTrue(result.isPresent());
        assertEquals("johnny", result.get().getUsername());
    }

    @Test
    void testUpdateUserWhenNotExists() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.updateUser(99L, sampleUser);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteUserWhenExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        boolean deleted = userService.deleteUser(1L);
        assertTrue(deleted);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUserWhenNotExists() {
        when(userRepository.existsById(2L)).thenReturn(false);

        boolean deleted = userService.deleteUser(2L);
        assertFalse(deleted);
        verify(userRepository, never()).deleteById(2L);
    }

    @Test
    void testGetUserByUsername() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(sampleUser));

        Optional<User> result = userService.getUserByUsername("john");
        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(sampleUser));

        Optional<User> result = userService.getUserByEmail("john@example.com");
        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().getEmail());
    }

    @Test
    void testExistsByEmail() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertTrue(userService.existsByEmail("john@example.com"));
    }

    @Test
    void testExistsByUsername() {
        when(userRepository.existsByUsername("john")).thenReturn(true);

        assertTrue(userService.existsByUsername("john"));
    }
}

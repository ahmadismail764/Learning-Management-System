package org.software.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.software.lms.dto.UserDto;
import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.Role;
import org.software.lms.model.User;
import org.software.lms.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock data
        userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password");

        user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setRole(Role.STUDENT);
    }

    @Test
    public void testCreateUser_success() {
        // Mock the behavior of userRepository.existsByEmail and userRepository.save
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call the method to test
        UserDto createdUser = userService.createUser(userDto);

        // Verify the result
        assertNotNull(createdUser);
        assertEquals(userDto.getFirstName(), createdUser.getFirstName());
        assertEquals(userDto.getLastName(), createdUser.getLastName());
        assertEquals(userDto.getEmail(), createdUser.getEmail());

        // Verify interactions with the mocks
        verify(userRepository, times(1)).existsByEmail(userDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUser_emailAlreadyExists() {
        // Mock the behavior of userRepository.existsByEmail
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        // Call the method and expect an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(userDto);
        });

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    public void testGetUserById_success() {
        // Mock the behavior of userRepository.findById
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Call the method to test
        UserDto userDto = userService.getUserById(1L);

        // Verify the result
        assertNotNull(userDto);
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getEmail(), userDto.getEmail());

        // Verify interactions with the mocks
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetUserById_userNotFound() {
        // Mock the behavior of userRepository.findById to return empty
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the method and expect an exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    public void testUpdateUser_success() {
        // Set up a new userDto for updating
        UserDto updateDto = new UserDto();
        updateDto.setFirstName("Updated Name");

        // Mock the behavior of userRepository.findById and userRepository.save
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call the method to test
        UserDto updatedUser = userService.updateUser(1L, updateDto);

        // Verify the result
        assertNotNull(updatedUser);
        assertEquals(updateDto.getFirstName(), updatedUser.getFirstName());

        // Verify interactions with the mocks
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser_userNotFound() {
        // Mock the behavior of userRepository.findById to return empty
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the method and expect an exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(1L, userDto);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    public void testDeleteUser_success() {
        // Mock the behavior of userRepository.findById
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Call the method to test
        userService.deleteUser(1L);

        // Verify interactions with the mocks
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    public void testDeleteUser_userNotFound() {
        // Mock the behavior of userRepository.findById to return empty
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the method and expect an exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }
}

package org.software.lms.service;

import org.software.lms.dto.UserDto;
import org.software.lms.model.Role;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email);
    List<UserDto> getUsersByRole(Role role);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    boolean existsByEmail(String email);
}

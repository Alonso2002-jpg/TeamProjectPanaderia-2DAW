package org.develop.TeamProjectPanaderia.rest.users.services;

import org.develop.TeamProjectPanaderia.rest.users.dto.UserInfoResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserRequestDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<UserResponseDto> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isActive, Pageable pageable);

    UserInfoResponseDto findById(Long id);
    Optional<User> findByUsername(String username);
    UserResponseDto save(UserRequestDto userRequestDto);
    UserResponseDto update(Long id, UserRequestDto userRequestDto);
    void deleteById(Long id);
}

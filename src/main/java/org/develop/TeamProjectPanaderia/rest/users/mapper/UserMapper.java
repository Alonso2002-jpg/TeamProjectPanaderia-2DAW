package org.develop.TeamProjectPanaderia.rest.users.mapper;

import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.rest.personal.models.Personal;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserInfoResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserRequestDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.model.Role;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    public User toUser(UserRequestDto request) {
        return User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(request.getRoles())
                .isActive(request.getIsActive())
                .build();
    }

    public User toUser(User user,UserRequestDto request, Long id) {
        return User.builder()
                .id(id)
                .name(request.getName() == null ? user.getName() : request.getName())
                .username(request.getUsername() == null ? user.getUsername() : request.getUsername())
                .email(request.getEmail() == null ? user.getEmail() : request.getEmail())
                .password(request.getPassword() == null ? passwordEncoder.encode(user.getPassword()) : passwordEncoder.encode(request.getPassword()))
                .roles(request.getRoles() == null ? user.getRoles() : request.getRoles())
                .isActive(request.getIsActive() == null ? user.getIsActive() : request.getIsActive())
                .build();
    }
    public User toUserFromPersonal(PersonalCreateDto personal, String password){
        return User.builder()
                .name(personal.nombre())
                .username(personal.dni())
                .password(passwordEncoder.encode(password))
                .email(personal.email())
                .roles(Set.of(Role.USER))
                .build();
    }
    public UserResponseDto toUserResponse(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(user.getRoles())
                .isActive(user.getIsActive())
                .build();
    }

    public UserInfoResponseDto toUserInfoResponse(User user, List<String> pedidos){
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isActive(user.getIsActive())
                .pedidos(pedidos)
                .build();

    }
}

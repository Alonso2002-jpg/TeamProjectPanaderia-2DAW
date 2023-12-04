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

/**
 * Clase encargada de mapear entre objetos relacionados con usuarios, como User y UserRequestDto.
 * Utiliza un PasswordEncoder para cifrar las contraseñas al mapear.
 */
@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor de la clase UserMapper.
     *
     * @param passwordEncoder El codificador de contraseñas utilizado para cifrar las contraseñas.
     */
    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Convierte un UserRequestDto en un objeto User.
     *
     * @param request El objeto UserRequestDto que contiene la información del usuario a crear.
     * @return Un objeto User creado a partir de la información proporcionada en el UserRequestDto.
     */
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

    /**
     * Convierte un User existente y un UserRequestDto en un nuevo objeto User actualizado.
     *
     * @param user    El usuario existente que se va a actualizar.
     * @param request El objeto UserRequestDto que contiene la información actualizada del usuario.
     * @param id      El identificador único del usuario.
     * @return Un objeto User actualizado con la información proporcionada en el UserRequestDto.
     */
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

    /**
     * Convierte un PersonalCreateDto y una contraseña en un objeto User.
     *
     * @param personal El objeto PersonalCreateDto que contiene la información personal del usuario.
     * @param password La contraseña del usuario.
     * @return Un objeto User creado a partir de la información proporcionada en el PersonalCreateDto.
     */
    public User toUserFromPersonal(PersonalCreateDto personal, String password){
        return User.builder()
                .name(personal.nombre())
                .username(personal.dni())
                .password(passwordEncoder.encode(password))
                .email(personal.email())
                .roles(Set.of(Role.USER))
                .build();
    }

    /**
     * Convierte un objeto User en un UserResponseDto.
     *
     * @param user El objeto User del cual se generará la respuesta.
     * @return Un objeto UserResponseDto con la información del usuario.
     */
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

    /**
     * Convierte un objeto User y una lista de pedidos en un UserInfoResponseDto.
     *
     * @param user    El objeto User del cual se generará la respuesta.
     * @param pedidos Lista de identificadores de pedidos asociados al usuario.
     * @return Un objeto UserInfoResponseDto con la información del usuario y sus pedidos.
     */
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

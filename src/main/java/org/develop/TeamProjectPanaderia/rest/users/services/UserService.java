package org.develop.TeamProjectPanaderia.rest.users.services;

import org.develop.TeamProjectPanaderia.rest.users.dto.UserInfoResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserRequestDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interfaz que define los servicios relacionados con la gestión de usuarios.
 */
public interface UserService {

    /**
     * Obtiene una página de usuarios filtrados opcionalmente por nombre de usuario, correo electrónico y estado de activación.
     *
     * @param username Nombre de usuario opcional para filtrar.
     * @param email    Dirección de correo electrónico opcional para filtrar.
     * @param isActive Estado de activación opcional para filtrar.
     * @param pageable Objeto Pageable para la paginación y ordenación de los resultados.
     * @return Página de UserResponseDto que cumple con los criterios de filtrado.
     */
    Page<UserResponseDto> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isActive, Pageable pageable);

    /**
     * Obtiene la información detallada de un usuario por su identificador único.
     *
     * @param id Identificador único del usuario.
     * @return UserInfoResponseDto que contiene la información detallada del usuario.
     */
    UserInfoResponseDto findById(Long id);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    Optional<User> findByUsername(String username);

    /**
     * Guarda un nuevo usuario en el sistema.
     *
     * @param userRequestDto Datos del usuario a ser guardado.
     * @return UserResponseDto que contiene la información del usuario guardado.
     */
    UserResponseDto save(UserRequestDto userRequestDto);

    /**
     * Actualiza la información de un usuario existente por su identificador único.
     *
     * @param id             Identificador único del usuario a actualizar.
     * @param userRequestDto Nuevos datos del usuario.
     * @return UserResponseDto que contiene la información del usuario actualizado.
     */
    UserResponseDto update(Long id, UserRequestDto userRequestDto);

    /**
     * Elimina un usuario por su identificador único.
     *
     * @param id Identificador único del usuario a eliminar.
     */
    void deleteById(Long id);
}

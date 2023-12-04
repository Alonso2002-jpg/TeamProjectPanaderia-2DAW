package org.develop.TeamProjectPanaderia.rest.users.services;

import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.rest.pedidos.repositories.PedidoRepository;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserInfoResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserRequestDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.exceptions.UserNotFound;
import org.develop.TeamProjectPanaderia.rest.users.exceptions.UsernameOrEmailExists;
import org.develop.TeamProjectPanaderia.rest.users.mapper.UserMapper;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.develop.TeamProjectPanaderia.rest.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementación de la interfaz UserService que gestiona las operaciones relacionadas con usuarios.
 */
@Service
@Slf4j
@CacheConfig(cacheNames = {"users"})
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PedidoRepository pedidoRepository;
    private final UserMapper userMapper;

    /**
     * Constructor de UserServiceImpl.
     *
     * @param userRepository     Repositorio de usuarios.
     * @param userMapper         Mapper para convertir entre DTOs y entidades de usuario.
     * @param pedidoRepository   Repositorio de pedidos asociados a usuarios.
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PedidoRepository pedidoRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Obtiene una página de usuarios filtrados opcionalmente por nombre de usuario, correo electrónico y estado de activación.
     *
     * @param username Nombre de usuario opcional para filtrar.
     * @param email    Dirección de correo electrónico opcional para filtrar.
     * @param isActive Estado de activación opcional para filtrar.
     * @param pageable Objeto Pageable para la paginación y ordenación de los resultados.
     * @return Página de UserResponseDto que cumple con los criterios de filtrado.
     */
    @Override
    public Page<UserResponseDto> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isActive, Pageable pageable) {
        Specification<User> specUsername = ((root, query, criteriaBuilder) ->
                username.map(us -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + us.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        Specification<User> specEmail = ((root, query, criteriaBuilder) ->
                email.map(em -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + em.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        Specification<User> specIsActive = ((root, query, criteriaBuilder) ->
                isActive.map(is -> criteriaBuilder.equal(root.get("isActive"), is))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        Specification<User> criterial = Specification.where(specUsername)
                .and(specEmail)
                .and(specIsActive);

        return userRepository.findAll(criterial,pageable).map(userMapper::toUserResponse);
    }

    /**
     * Obtiene la información detallada de un usuario por su identificador único.
     *
     * @param id Identificador único del usuario.
     * @return UserInfoResponseDto que contiene la información detallada del usuario.
     */
    @Override
    public UserInfoResponseDto findById(Long id) {
        log.info("Obteniendo usuario con ID: {}", id);
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFound(" id " + id));
        var pedidos = pedidoRepository.findPedidosByIdUsuario(user.getId()).stream().map(p -> p.getId().toHexString()).toList();
        return userMapper.toUserInfoResponse(user,pedidos);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    /**
     * Guarda un nuevo usuario en el sistema.
     *
     * @param userRequestDto Datos del usuario a ser guardado.
     * @return UserResponseDto que contiene la información del usuario guardado.
     */
    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        log.info("Guardando usuario: " + userRequestDto);

        userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(userRequestDto.getUsername(), userRequestDto.getEmail())
                .ifPresent(user -> {
                    throw new UsernameOrEmailExists(user.getUsername() + "-" + user.getEmail());
                });
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(userRequestDto)));
    }

    /**
     * Actualiza la información de un usuario existente por su identificador único.
     *
     * @param id             Identificador único del usuario a actualizar.
     * @param userRequestDto Nuevos datos del usuario.
     * @return UserResponseDto que contiene la información del usuario actualizado.
     */
    @Override
    @CachePut(key = "#result.id")
    public UserResponseDto update(Long id, UserRequestDto userRequestDto) {
        log.info("Actualizando usuario: " + userRequestDto);
        var userfound = userRepository.findById(id).orElseThrow(() -> new UserNotFound("id " + id));

        userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(userRequestDto.getUsername(), userRequestDto.getEmail())
                .ifPresent(user -> {
                    if (!user.getId().equals(id)) {
                        throw new UsernameOrEmailExists(user.getUsername() + "-" + user.getEmail());
                    }
                });
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(userfound,userRequestDto,id)));
    }

    /**
     * Elimina un usuario por su identificador único.
     *
     * @param id Identificador único del usuario a eliminar.
     */
    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        userRepository.findById(id).orElseThrow(() -> new UserNotFound("id " + id));

        if (pedidoRepository.existsByIdUsuario(id)){
            log.info("Borrado logido de usuario por id: " + id);
            userRepository.updateIsActiveToFalseById(id);
        }else {
            log.info("Eliminando usuario por id: " + id);
            userRepository.deleteById(id);
        }

    }
}

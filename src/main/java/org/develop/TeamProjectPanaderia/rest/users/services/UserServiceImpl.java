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

@Service
@Slf4j
@CacheConfig(cacheNames = {"users"})
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PedidoRepository pedidoRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PedidoRepository pedidoRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.pedidoRepository = pedidoRepository;
    }
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

    @Override
    public UserInfoResponseDto findById(Long id) {
        log.info("Obteniendo usuario con ID: {}", id);
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFound(" id " + id));
        var pedidos = pedidoRepository.findPedidosByIdUsuario(user.getId()).stream().map(p -> p.getId().toHexString()).toList();
        return userMapper.toUserInfoResponse(user,pedidos);
    }

    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        log.info("Guardando usuario: " + userRequestDto);

        userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(userRequestDto.getUsername(), userRequestDto.getEmail())
                .ifPresent(user -> {
                    throw new UsernameOrEmailExists(user.getUsername() + "-" + user.getEmail());
                });
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(userRequestDto)));
    }

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

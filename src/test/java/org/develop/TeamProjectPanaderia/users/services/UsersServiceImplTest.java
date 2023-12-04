package org.develop.TeamProjectPanaderia.users.services;

import org.develop.TeamProjectPanaderia.rest.pedidos.repositories.PedidoRepository;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserInfoResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserRequestDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.exceptions.UserNotFound;
import org.develop.TeamProjectPanaderia.rest.users.exceptions.UsernameOrEmailExists;
import org.develop.TeamProjectPanaderia.rest.users.mapper.UserMapper;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.develop.TeamProjectPanaderia.rest.users.repositories.UserRepository;
import org.develop.TeamProjectPanaderia.rest.users.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {
    private final UserRequestDto userRequest = UserRequestDto.builder().username("test").email("test@pruebita.com").build();
    private final User user = User.builder().id(1L).username("test").email("test@pruebita.com").build();
    private final UserResponseDto userResponse = UserResponseDto.builder().username("test").email("test@pruebita.com").build();
    private final UserInfoResponseDto userInfoResponse = UserInfoResponseDto.builder().username("test").email("test@pruebita.com").build();
    @Mock
    private UserRepository usersRepository;
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private UserMapper usersMapper;
    @InjectMocks
    private UserServiceImpl usersService;

    @Test
    void findAll_NotFilters(){
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        Page<User> page = new PageImpl<>(users);


        when(usersRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(usersMapper.toUserResponse(any(User.class))).thenReturn(new UserResponseDto());

        Page <UserResponseDto> response = usersService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Pageable.unpaged());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(2, response.getTotalElements())
        );

        verify(usersRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findById(){
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidoRepository.findPedidosByIdUsuario(userId)).thenReturn(List.of());
        when(usersMapper.toUserInfoResponse(any(User.class), anyList())).thenReturn(userInfoResponse);

        UserInfoResponseDto response = usersService.findById(userId);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(userResponse.getUsername(), response.getUsername()),
                () -> assertEquals(userResponse.getEmail(), response.getEmail())
        );

        verify(usersRepository, times(1)).findById(userId);
        verify(pedidoRepository, times(1)).findPedidosByIdUsuario(userId);
        verify(usersMapper, times(1)).toUserInfoResponse(user, List.of());
    }


    @Test
    void findById_NotFound(){
        Long userId = 100L;

        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        var result = assertThrows(UserNotFound.class, () -> usersService.findById(userId));
        assertEquals("User with  id " + userId + " not found", result.getMessage());

        verify(usersRepository, times(1)).findById(userId);
    }

    @Test
    void save_true(){
        when(usersRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(usersMapper.toUser(userRequest)).thenReturn(user);
        when(usersMapper.toUserResponse(user)).thenReturn(userResponse);
        when(usersRepository.save(user)).thenReturn(user);

        UserResponseDto response = usersService.save(userRequest);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(userRequest.getUsername(), response.getUsername()),
                () -> assertEquals(userRequest.getEmail(), response.getEmail())
        );

        verify(usersMapper, times(1)).toUser(userRequest);
        verify(usersMapper, times(1)).toUserResponse(user);
        verify(usersRepository, times(1)).findByUsernameIgnoreCaseOrEmailIgnoreCase(any(String.class), any(String.class));
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    void save_false_usernameOrEmailExists(){
        when(usersRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(new User()));

        assertThrows(UsernameOrEmailExists.class, () -> usersService.save(userRequest));
    }

    @Test
    void update_true(){
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(usersRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(usersMapper.toUser(user, userRequest, userId)).thenReturn(user);
        when(usersMapper.toUserResponse(user)).thenReturn(userResponse);
        when(usersRepository.save(user)).thenReturn(user);

        UserResponseDto response = usersService.update(userId, userRequest);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(userRequest.getUsername(), response.getUsername()),
                () -> assertEquals(userRequest.getEmail(), response.getEmail())
        );

        verify(usersMapper, times(1)).toUserResponse(user);
        verify(usersRepository, times(1)).save(user);
        verify(usersRepository, times(1)).findById(userId);
        verify(usersRepository, times(1)).findByUsernameIgnoreCaseOrEmailIgnoreCase(any(String.class), any(String.class));
        verify(usersMapper, times(1)).toUser(user, userRequest, userId);
    }

    @Test
    void update_false_UsernameOrEmailExist(){
        Long userId = 5L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(usersRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(user));

        assertThrows(UsernameOrEmailExists.class, () -> usersService.update(userId, userRequest));
    }


    @Test
    void update_false_userNotFound(){
        Long userId = 100L;
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        var result = assertThrows(UserNotFound.class, () -> usersService.update(userId, userRequest));
        assertEquals("User with id " + userId + " not found", result.getMessage());

        verify(usersRepository, times(1)).findById(userId);
    }

    @Test
    void deleteById_phisical(){
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidoRepository.existsByIdUsuario(userId)).thenReturn(false);

        usersService.deleteById(userId);

        verify(usersRepository, times(1)).findById(userId);
        verify(pedidoRepository, times(1)).existsByIdUsuario(userId);
    }

    @Test
    void deleteById_logical(){
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidoRepository.existsByIdUsuario(userId)).thenReturn(true);
        doNothing().when(usersRepository).updateIsActiveToFalseById(userId);

        usersService.deleteById(userId);

        verify(usersRepository, times(1)).updateIsActiveToFalseById(userId);
        verify(pedidoRepository, times(1)).existsByIdUsuario(userId);
    }

    @Test
    void deleteById_false_userNotFound(){
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        var result = assertThrows(UserNotFound.class, () -> usersService.deleteById(userId));
        assertEquals("User with id " + userId + " not found", result.getMessage());
    }
}

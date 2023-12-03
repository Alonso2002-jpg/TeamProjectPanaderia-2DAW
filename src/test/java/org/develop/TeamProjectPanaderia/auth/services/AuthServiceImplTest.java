package org.develop.TeamProjectPanaderia.auth.services;


import org.develop.TeamProjectPanaderia.rest.auth.dto.JwtAuthResponseDto;
import org.develop.TeamProjectPanaderia.rest.auth.dto.UserSignInRequest;
import org.develop.TeamProjectPanaderia.rest.auth.dto.UserSignUpRequest;
import org.develop.TeamProjectPanaderia.rest.auth.exceptions.AuthSignInInvalid;
import org.develop.TeamProjectPanaderia.rest.auth.exceptions.UserInvalidPasswords;
import org.develop.TeamProjectPanaderia.rest.auth.repositories.AuthRepository;
import org.develop.TeamProjectPanaderia.rest.auth.services.authentication.AuthenticationServiceImpl;
import org.develop.TeamProjectPanaderia.rest.auth.services.jwt.JwtService;
import org.develop.TeamProjectPanaderia.rest.users.exceptions.UsernameOrEmailExists;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AuthRepository authRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void signUp_true(){
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("username_test");
        request.setPassword("password_test");
        request.setPasswordComprobacion("password_test");
        request.setEmail("test@test.com");
        request.setName("Testcito");

        User userStored = new User();
        String token = "token_prueba";

        when(authRepository.save(any(User.class))).thenReturn(userStored);

        when(jwtService.generateToken(userStored)).thenReturn(token);

        JwtAuthResponseDto response = authenticationService.signUp(request);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(token, response.getToken()),
                () -> verify(authRepository, times(1)).save(any(User.class)),
                () -> verify(jwtService, times(1)).generateToken(userStored)
        );
    }

    @Test
    void signUp_false_differentsPassword()  {
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("username_test");
        request.setPassword("password_test");
        request.setPasswordComprobacion("password_test_falso");
        request.setEmail("test@test.com");
        request.setName("Testcito");

        assertThrows(UserInvalidPasswords.class, () -> authenticationService.signUp(request));
    }

    @Test
    void signUp_false_NameOrEmailExists(){
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("username_test");
        request.setPassword("password_test");
        request.setPasswordComprobacion("password_test");
        request.setEmail("test@test.com");
        request.setName("Testcito");

        when(authRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(UsernameOrEmailExists.class, () -> authenticationService.signUp(request));
    }

    @Test
    void signIn_true (){
        UserSignInRequest request = new UserSignInRequest();
        request.setUsername("pericoodelospalotes");
        request.setPassword("123");

        User user = new User();
        when(authRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        String token = "token_prueba";
        when(jwtService.generateToken(user)).thenReturn(token);

        JwtAuthResponseDto response = authenticationService.signIn(request);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(token, response.getToken()),
                () -> verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class)),
                () -> verify(authRepository, times(1)).findByUsername(request.getUsername()),
                () -> verify(jwtService, times(1)).generateToken(user)
        );
    }

    @Test
    void signIn_false_InvalidUsernameOrPassword(){
        UserSignInRequest request = new UserSignInRequest();
        request.setUsername("pericoodelospalotes");
        request.setPassword("123");

        when(authRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        assertThrows(AuthSignInInvalid.class, () -> authenticationService.signIn(request));
    }
}
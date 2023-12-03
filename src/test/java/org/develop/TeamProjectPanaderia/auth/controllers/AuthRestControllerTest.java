package org.develop.TeamProjectPanaderia.auth.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.develop.TeamProjectPanaderia.rest.auth.dto.JwtAuthResponseDto;
import org.develop.TeamProjectPanaderia.rest.auth.dto.UserSignInRequest;
import org.develop.TeamProjectPanaderia.rest.auth.dto.UserSignUpRequest;
import org.develop.TeamProjectPanaderia.rest.auth.exceptions.AuthSignInInvalid;
import org.develop.TeamProjectPanaderia.rest.auth.exceptions.UserInvalidPasswords;
import org.develop.TeamProjectPanaderia.rest.auth.services.authentication.AuthenticationService;
import org.develop.TeamProjectPanaderia.rest.users.exceptions.UsernameOrEmailExists;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthRestControllerTest {
    private final String myEndpoint = "/v1/auth";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private AuthenticationService authService;

    @Autowired
    public AuthRestControllerTest(AuthenticationService authService) {
        this.authService = authService;
        mapper.registerModule(new JavaTimeModule());
    }


    @Test
    void signUp_true() throws Exception {
        var userSignUpRequest = UserSignUpRequest.builder()
                .name("Test1")
                .username("username_test")
                .email("test@test.com")
                .password("password_test")
                .passwordComprobacion("password_test")
                .build();
        var jwtAuthResponse = new JwtAuthResponseDto("token");
        var myLocalEndpoint = myEndpoint + "/signup";

        when(authService.signUp(any(UserSignUpRequest.class))).thenReturn(jwtAuthResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userSignUpRequest)))
                .andReturn().getResponse();

        JwtAuthResponseDto res = mapper.readValue(response.getContentAsString(), JwtAuthResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("token", res.getToken())
        );

        // Verify
        verify(authService, times(1)).signUp(any(UserSignUpRequest.class));
    }

    @Test
    void signUp_false_DiferentsPasswords() {
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("username_test");
        request.setPassword("password_test");
        request.setPasswordComprobacion("contrasena_test");
        request.setEmail("test@test.com");
        request.setName("nombre_test");

        when(authService.signUp(any(UserSignUpRequest.class))).thenThrow(new UserInvalidPasswords("Passwords don't match"));

        assertThrows(UserInvalidPasswords.class, () -> authService.signUp(request));

        // Verify
        verify(authService, times(1)).signUp(any(UserSignUpRequest.class));
    }

    @Test
    void signUp_false_usernameOrEmailExist() {
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("username_test");
        request.setPassword("password_test");
        request.setPasswordComprobacion("password_test");
        request.setEmail("test@test.com");
        request.setName("nombre_test");

        when(authService.signUp(any(UserSignUpRequest.class))).thenThrow(new UsernameOrEmailExists("Username or email already exists"));

        assertThrows(UsernameOrEmailExists.class, () -> authService.signUp(request));

        // Verify
        verify(authService, times(1)).signUp(any(UserSignUpRequest.class));
    }


    @Test
    void signUp_false_EmptyFields() throws Exception {
        var myLocalEndpoint = myEndpoint + "/signup";

        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("");
        request.setPassword("password_test");
        request.setPasswordComprobacion("password_test");
        request.setEmail("");
        request.setName("");

        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("Nombre no puede estar")),
                () -> assertTrue(response.getContentAsString().contains("Username no puede"))
        );
    }

    @Test
    void signIn_true() throws Exception {
        var userSignUpRequest = new UserSignUpRequest("Test1", "username_test", "test@test.com", "password_test", "password_test");
        var jwtAuthResponse = new JwtAuthResponseDto("token");

        var myLocalEndpoint = myEndpoint + "/signin";

        when(authService.signIn(any(UserSignInRequest.class))).thenReturn(jwtAuthResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userSignUpRequest)))
                .andReturn().getResponse();

        JwtAuthResponseDto res = mapper.readValue(response.getContentAsString(), JwtAuthResponseDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("token", res.getToken())
        );

        verify(authService, times(1)).signIn(any(UserSignInRequest.class));
    }


    @Test
    void signIn_false_IncorrectUsernameOrPassword() {
        var myLocalEndpoint = myEndpoint + "/signin";

        UserSignInRequest request = new UserSignInRequest("username_falso", "password_falso");

        when(authService.signIn(any(UserSignInRequest.class))).thenThrow(new AuthSignInInvalid("Usuario o contraseña incorrectos"));

        assertThrows(AuthSignInInvalid.class, () -> authService.signIn(request));

        verify(authService, times(1)).signIn(any(UserSignInRequest.class));
    }


    @Test
    void signIn_false_EmptyUsernameAndPassword() throws Exception {
        var myLocalEndpoint = myEndpoint + "/signin";
        UserSignInRequest request = UserSignInRequest.builder()
                .username("")
                .password("")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
    }
}
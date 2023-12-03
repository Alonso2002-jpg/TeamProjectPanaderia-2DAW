package org.develop.TeamProjectPanaderia.users.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserInfoResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserRequestDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.exceptions.UserNotFound;
import org.develop.TeamProjectPanaderia.rest.users.exceptions.UsernameOrEmailExists;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.develop.TeamProjectPanaderia.rest.users.services.UserService;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class UsersRestControllerTest {

    private final UserRequestDto userRequest = UserRequestDto.builder()
            .name("test perez")
            .password("password_test")
            .username("username_test")
            .email("test@test.com")
            .build();

    private final User user = User.builder()
            .id(50L)
            .name("test perez")
            .password("password_test")
            .username("username_test")
            .email("test@test.com")
            .build();

    private final UserResponseDto userResponse = UserResponseDto.builder()
            .id(50L)
            .name("test perez")
            .username("username_test")
            .email("test@test.com")
            .build();

    private final UserInfoResponseDto userInfoResponse = UserInfoResponseDto.builder()
            .id(50L)
            .name("test perez")
            .username("username_test")
            .email("test@test.com")
            .build();

    private final String myEndPoint = "/v1/users";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Autowired
    public UsersRestControllerTest(UserService userService){
        this.userService = userService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithAnonymousUser
    void authenticate_false() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint).accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(403, response.getStatus());
    }

    @Test
    void findAllUsers() throws Exception {
        var lista = List.of(userResponse);
        Page<UserResponseDto> page = new PageImpl<>(lista);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(userService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<UserResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        verify(userService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void findById() throws Exception {
        String myLocalEndPoint = myEndPoint + "/1";

        when(userService.findById(1L)).thenReturn(userInfoResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        var result = mapper.readValue(response.getContentAsString(), UserInfoResponseDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(userInfoResponse, result)
        );

        verify(userService, times(1)).findById(1L);
    }

    @Test
    void findById_notFound() throws Exception {
        String myLocalEndPoint = myEndPoint + "/100";
        Long id = 100L;

        when(userService.findById(id)).thenThrow(new UserNotFound(" id " + id));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(userService, times(1)).findById(id);
    }


    @Test
    void createUser() throws Exception {
        when(userService.save(userRequest)).thenReturn(userResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndPoint).accept(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), UserResponseDto.class);

        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(userResponse, res)
        );

        verify(userService, times(1)).save(userRequest);
    }

    @Test
    void createUser_false_UserNameOrEmailExists() throws Exception {
        when(userService.save(userRequest)).thenThrow(new UsernameOrEmailExists("El nombre de usuario o el email ya existen"));

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndPoint).accept(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());

        verify(userService, times(1)).save(userRequest);
    }


    @Test
    void createUser_false_BadRequest() throws Exception {
        UserRequestDto falseUserRequest = UserRequestDto.builder()
                .name("")
                .email("test@test.com")
                .password("test")
                .username("")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndPoint).accept(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(falseUserRequest)))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
    }

    @Test
    void updateUser() throws Exception {
        String myLocalEndPoint = myEndPoint + "/1";

        when(userService.update(1L, userRequest)).thenReturn(userResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        var result = mapper.readValue(response.getContentAsString(), UserResponseDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(userResponse, result)
        );

        verify(userService, times(1)).update(1L, userRequest);

    }

    @Test
    void deleteUser() throws Exception {
        String myLocalEndPoint = myEndPoint + "/1";

        doNothing().when(userService).deleteById(1L);

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(204, response.getStatus());

        verify(userService, times(1)).deleteById(1L);
    }

    @Test
    @WithUserDetails("admin")
    void testMe() throws Exception {
        String myLocalEndPoint = myEndPoint + "/me/profile";

        when(userService.findById(1L)).thenReturn(userInfoResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        verify(userService, times(1)).findById(1L);
    }

    @Test
    @WithAnonymousUser
    void testMe_false() throws Exception {
        String myLocalEndPoint = myEndPoint + "/me/profile";

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(403, response.getStatus());
    }


    @Test
    @WithUserDetails("12345678A")
    void deleteMe_true() throws Exception{
        String myLocalEndPoint = myEndPoint + "/me/profile";

        doNothing().when(userService).deleteById(anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(204, response.getStatus());
    }

    @Test
    @WithUserDetails("12345678A")
    void updateMe_true() throws Exception {
        String myLocalEndPoint = myEndPoint + "/me/profile";

        when(userService.update(3L, userRequest)).thenReturn(userResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        var result = mapper.readValue(response.getContentAsString(), UserResponseDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(userResponse, result)
        );

        verify(userService, times(1)).update(3L, userRequest);

    }


}

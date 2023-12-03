package org.develop.TeamProjectPanaderia.Personal.controllers;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalResponseDto;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.rest.personal.exceptions.PersonalBadUuid;
import org.develop.TeamProjectPanaderia.rest.personal.exceptions.PersonalNotFoundException;
import org.develop.TeamProjectPanaderia.rest.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.rest.personal.models.Personal;
import org.develop.TeamProjectPanaderia.rest.personal.services.PersonalService;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoResponseDto;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoBadRequest;
import org.develop.TeamProjectPanaderia.rest.users.model.Role;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class PersonalRestControllerTest {
    private final String myEndpoint = "/v1/personal";
    private final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private final PersonalMapper personalMapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private PersonalService personalService;
    @Autowired
    private JacksonTester<PersonalCreateDto> jsonPersonalCreateDto;
    @Autowired
    private JacksonTester <PersonalUpdateDto> jsonPersonalUpdateDto;
    private final Categoria categoriaPersonal = new Categoria(1L, "PERSONAL_TEST", LocalDate.now(), LocalDate.now(), true);
    private Personal personal1;
    private Personal personal2;
    private PersonalResponseDto personalResponseDto1;
    private PersonalResponseDto personalResponseDto2;
    private User user1;
    private User user2;

    @Autowired
    public PersonalRestControllerTest(PersonalMapper personalMapper, PersonalService personalService){
        this.personalMapper = personalMapper;
        this.personalService = personalService;
        mapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setup(){
        user1 = new User(1L, "TEST-1", "56789125E", "prueba1@prueba.com", "prueba123", LocalDateTime.now(), LocalDateTime.now(), true, Set.of(Role.ADMIN, Role.USER));
        user2 = new User(2L, "TEST-2", "56781236E", "prueba2@prueba.com", "prueba123", LocalDateTime.now(), LocalDateTime.now(), true, Set.of(Role.ADMIN, Role.USER));
        personal1 = new Personal(UUID.randomUUID(),"TEST-1", "56789125E", "prueba1@prueba.com", LocalDate.now(), LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), true, user1, categoriaPersonal);
        personal2 = new Personal(UUID.randomUUID(),"TEST-2", "56781236E", "prueba2@prueba.com", LocalDate.now(), LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), true, user2, categoriaPersonal);
        personalResponseDto1 = PersonalResponseDto.builder()
                .id(personal1.getId())
                .dni(personal1.getDni())
                .nombre(personal1.getNombre())
                .seccion(personal1.getSeccion().getNameCategory())
                .fechaAlta(personal1.getFechaAlta().toString())
                .isActive(personal1.isActive())
                .build();
        personalResponseDto2 = PersonalResponseDto.builder()
                .id(personal2.getId())
                .dni(personal2.getDni())
                .nombre(personal2.getNombre())
                .seccion(personal2.getSeccion().getNameCategory())
                .fechaAlta(personal2.getFechaAlta().toString())
                .isActive(personal2.isActive())
                .build();
    }

    @Test
    void getAllPersonal() throws Exception {
        List <Personal> personalList = List.of(personal1, personal2);
        List <PersonalResponseDto> expectedPersonal = List.of(personalResponseDto1, personalResponseDto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page <Personal> responsePage = new PageImpl<>(personalList);
        Page <PersonalResponseDto> responseDto = new PageImpl<>(expectedPersonal);


        // Arrange
        when(personalService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),  pageable)).thenReturn(responsePage);
        when(personalMapper.toPageResponse(responsePage)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<PersonalResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(personalService, times(1)).findAll(Optional.empty(), Optional.empty(),  Optional.empty(), Optional.empty(), pageable);
        verify(personalMapper, times(1)).toPageResponse(responsePage);
    }


    @Test
    void getAllPersonal_ByNombre() throws Exception {
        Optional<String> nombre = Optional.of("TEST-1");
        String myLocalEndPoint = myEndpoint + "?nombre=TEST-1";
        List <Personal> personalList = List.of(personal1);
        List <PersonalResponseDto> expectedPersonal = List.of(personalResponseDto1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page <Personal> responsePage = new PageImpl<>(personalList);
        Page <PersonalResponseDto> responseDto = new PageImpl<>(expectedPersonal);


        // Arrange
        when(personalService.findAll(nombre, Optional.empty(), Optional.empty(), Optional.empty(),  pageable)).thenReturn(responsePage);
        when(personalMapper.toPageResponse(responsePage)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<PersonalResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        // Verify
        verify(personalService, times(1)).findAll(nombre, Optional.empty(),  Optional.empty(), Optional.empty(), pageable);
        verify(personalMapper, times(1)).toPageResponse(responsePage);
    }

    @Test
    void getAllPersonal_ByDni() throws Exception {
        Optional<String> dni = Optional.of("56781236E");
        String myLocalEndPoint = myEndpoint + "?dni=56781236E";
        List <Personal> personalList = List.of(personal1);
        List <PersonalResponseDto> expectedPersonal = List.of(personalResponseDto1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page <Personal> responsePage = new PageImpl<>(personalList);
        Page <PersonalResponseDto> responseDto = new PageImpl<>(expectedPersonal);


        // Arrange
        when(personalService.findAll(Optional.empty(), dni, Optional.empty(), Optional.empty(),  pageable)).thenReturn(responsePage);
        when(personalMapper.toPageResponse(responsePage)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<PersonalResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        // Verify
        verify(personalService, times(1)).findAll(Optional.empty(), dni,  Optional.empty(), Optional.empty(), pageable);
        verify(personalMapper, times(1)).toPageResponse(responsePage);
    }


    @Test
    void getAllPersonal_ByCategoria() throws Exception {
        Optional<String> categoria = Optional.of("PERSONAL_TEST");
        String myLocalEndPoint = myEndpoint + "?categoria=PERSONAL_TEST";
        List <Personal> personalList = List.of(personal1);
        List <PersonalResponseDto> expectedPersonal = List.of(personalResponseDto1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page <Personal> responsePage = new PageImpl<>(personalList);
        Page <PersonalResponseDto> responseDto = new PageImpl<>(expectedPersonal);


        // Arrange
        when(personalService.findAll(Optional.empty(), Optional.empty(), categoria, Optional.empty(),  pageable)).thenReturn(responsePage);
        when(personalMapper.toPageResponse(responsePage)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<PersonalResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        // Verify
        verify(personalService, times(1)).findAll(Optional.empty(), Optional.empty(),  categoria, Optional.empty(), pageable);
        verify(personalMapper, times(1)).toPageResponse(responsePage);
    }


    @Test
    void getAllPersonal_ByIsActivo() throws Exception {
        Optional<Boolean> isActivo = Optional.of(true);
        String myLocalEndPoint = myEndpoint + "?isActivo=true";
        List <Personal> personalList = List.of(personal1);
        List <PersonalResponseDto> expectedPersonal = List.of(personalResponseDto1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page <Personal> responsePage = new PageImpl<>(personalList);
        Page <PersonalResponseDto> responseDto = new PageImpl<>(expectedPersonal);


        // Arrange
        when(personalService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), isActivo,  pageable)).thenReturn(responsePage);
        when(personalMapper.toPageResponse(responsePage)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<PersonalResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        // Verify
        verify(personalService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), isActivo, pageable);
        verify(personalMapper, times(1)).toPageResponse(responsePage);
    }


    @Test
    void getPersonalById() throws Exception {
        // Arrange
        String uuid = personal1.getId().toString();
        String myLocalEndPoint = myEndpoint + "/" + uuid;

        when(personalService.findById(uuid)).thenReturn(personal1);
        when(personalMapper.toResponseDto(personal1)).thenReturn(personalResponseDto1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PersonalResponseDto result = mapper.readValue(response.getContentAsString(), PersonalResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(personalResponseDto1, result)
        );

        // verify
        verify(personalService, times(1)).findById(uuid);
        verify(personalMapper, times(1)).toResponseDto(personal1);
    }


    @Test
    void getPersonalById_idNotExists() throws Exception {
        // Arrange
        UUID uuid = UUID.randomUUID();
        String uuidFalso = uuid.toString();
        String myLocalEndPoint = myEndpoint + "/" + uuidFalso;

        when(personalService.findById(uuidFalso)).thenThrow(new PersonalNotFoundException(uuid));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(404, response.getStatus());

        // verify
        verify(personalService, times(1)).findById(uuidFalso);
    }


    @Test
    void getProductById_InvalidUuid() throws Exception {
        // Arrange
        String uuidInvalido = "uuid_invalido";
        String myLocalEndPoint = myEndpoint + "/" + uuidInvalido;

        when(personalService.findById(uuidInvalido)).thenThrow(new PersonalBadUuid(uuidInvalido));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(400, response.getStatus());

        // verify
        verify(personalService, times(1)).findById(uuidInvalido);
    }


    @Test
    void createPersonal() throws Exception{
        // Arrange
        UUID uuid = UUID.randomUUID();
        PersonalCreateDto personalCreateDto = new PersonalCreateDto("56789125E", "Nuevo_Personal", "nuevopersonal@gmail.com", categoriaPersonal.getNameCategory(), true);

        when(personalService.save(personalCreateDto)).thenReturn(personal1);
        when(personalMapper.toResponseDto(personal1)).thenReturn(personalResponseDto1);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPersonalCreateDto.write(personalCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PersonalResponseDto result = mapper.readValue(response.getContentAsString(), PersonalResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(personalResponseDto1, result)
        );

        // Verify
        verify(personalService, times(1)).save(personalCreateDto);
        verify(personalMapper, times(1)).toResponseDto(personal1);
    }

    @Test
    void createPersonal_BadRequest_Dni_isNull() throws Exception{
        // Arrange
        PersonalCreateDto personalCreateDto = new PersonalCreateDto(null, "Nuevo_Personal", "nuevopersonal@gmail.com", categoriaPersonal.getNameCategory(), true);


        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPersonalCreateDto.write(personalCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El dni no puede estar vacio"))
        );
    }

    @Test
    void createPersonal_BadRequest_Dni_isInvalid() throws Exception{
        // Arrange
        PersonalCreateDto personalCreateDto = new PersonalCreateDto("123", "Nuevo_Personal", "nuevopersonal@gmail.com", categoriaPersonal.getNameCategory(), true);


        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPersonalCreateDto.write(personalCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El DNI debe tener 8 numeros seguidos de una letra"))
        );
    }

    @Test
    void createPersonal_BadRequest_Name_2LettersOnly() throws Exception{
        // Arrange
        PersonalCreateDto personalCreateDto = new PersonalCreateDto("56789125E", "ho", "nuevopersonal@gmail.com", categoriaPersonal.getNameCategory(), true);


        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPersonalCreateDto.write(personalCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El nombre debe contener al menos 3 letras"))
        );
    }


    @Test
    void createPersonal_BadRequest_Name_isEmpty() throws Exception{
        // Arrange
        PersonalCreateDto personalCreateDto = new PersonalCreateDto("56789125E", null, "nuevopersonal@gmail.com", categoriaPersonal.getNameCategory(), true);


        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPersonalCreateDto.write(personalCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El nombre no puede estar vacio"))
        );
    }

    @Test
    void createPersonal_BadRequest_Category_isEmpty() throws Exception{
        // Arrange
        PersonalCreateDto personalCreateDto = new PersonalCreateDto("56789125E", "TEST-1", "nuevopersonal@gmail.com", "", true);


        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPersonalCreateDto.write(personalCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("La seccion no puede estar vacia"))
        );
    }

    @Test
    void createPersonal_BadRequest_Category_notExist() throws Exception{
        // Arrange
        PersonalCreateDto personalCreateDto = new PersonalCreateDto("56789125E", "TEST-1", "nuevopersonal@gmail.com","Categoria_Falsa", true);

        when(personalService.save(personalCreateDto)).thenThrow(new ProductoBadRequest("Categoria_falsa"));

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPersonalCreateDto.write(personalCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
    }


    @Test
    void updatePersonal() throws Exception {
        // Arrange
        String id = personal2.getId().toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        PersonalUpdateDto personalUpdateDto = new PersonalUpdateDto("personal_actualizado", categoriaPersonal.getNameCategory(), false);

        when(personalService.update(id, personalUpdateDto)).thenReturn(personal2);
        when(personalMapper.toResponseDto(personal2)).thenReturn(personalResponseDto2);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPersonalUpdateDto.write(personalUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PersonalResponseDto result = mapper.readValue(response.getContentAsString(), PersonalResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(personalResponseDto2, result)
        );

        // Verify
        verify(personalService, times(1)).update(id, personalUpdateDto);
        verify(personalMapper, times(1)).toResponseDto(personal2);
    }

    @Test
    void updatePersonal_NotFound() throws Exception {
        // Arrange
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        PersonalUpdateDto personalUpdateDto = new PersonalUpdateDto("personal_actualizado", categoriaPersonal.getNameCategory(), false);

        // Arrange
        when(personalService.update(id, personalUpdateDto)).thenThrow(new PersonalNotFoundException(uuid));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPersonalUpdateDto.write(personalUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(404, response.getStatus());
    }

    @Test
    void updatePersonal_BadRequest_Nombre() throws Exception {
        // Arrange
        UUID uuid = personal1.getId();
        String id = uuid.toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        PersonalUpdateDto personalUpdateDto = new PersonalUpdateDto("ab", categoriaPersonal.getNameCategory(), false);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPersonalUpdateDto.write(personalUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El nombre debe contener al menos 3 letras"))
        );
    }

    @Test
    void updatePersonal_BadRequest_Category_NotExist() throws Exception {
        // Arrange
        UUID uuid = personal1.getId();
        String id = uuid.toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        PersonalUpdateDto personalUpdateDto = new PersonalUpdateDto("personal_actualizado", categoriaPersonal.getNameCategory(), false);

        when(personalService.update(id, personalUpdateDto)).thenThrow(new ProductoBadRequest(personalUpdateDto.seccion()));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPersonalUpdateDto.write(personalUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
    }

    @Test
    void updatePartialPersonal() throws Exception {
        // Arrange
        String id = personal2.getId().toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        PersonalUpdateDto personalUpdateDto = new PersonalUpdateDto("personal_actualizado", categoriaPersonal.getNameCategory(), false);

        when(personalService.update(id, personalUpdateDto)).thenReturn(personal2);
        when(personalMapper.toResponseDto(personal2)).thenReturn(personalResponseDto2);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        patch(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPersonalUpdateDto.write(personalUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PersonalResponseDto result = mapper.readValue(response.getContentAsString(), PersonalResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(personalResponseDto2, result)
        );

        // Verify
        verify(personalService, times(1)).update(id, personalUpdateDto);
    }

    @Test
    void deletePersonalById() throws Exception {
        // Arrange
        UUID uuid = personal2.getId();
        String myLocalEndpoint = myEndpoint + "/" + uuid.toString();

        doNothing().when(personalService).deleteById(uuid.toString());

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(() -> assertEquals(204, response.getStatus()));

        // Verify
        verify(personalService, times(1)).deleteById(uuid.toString());
    }

    @Test
    void deletePersonalById_IdNotExist() throws Exception {
        // Arrange
        UUID uuid = UUID.randomUUID();
        String myLocalEndpoint = myEndpoint + "/" + uuid.toString();

        doThrow(new PersonalNotFoundException(uuid)).when(personalService).deleteById(uuid.toString());

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );

        // Verify
        verify(personalService, times(1)).deleteById(uuid.toString());
    }




}

package org.develop.TeamProjectPanaderia.Personal.mappers;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalResponseDto;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.rest.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.rest.personal.models.Personal;
import org.develop.TeamProjectPanaderia.rest.users.mapper.UserMapper;
import org.develop.TeamProjectPanaderia.rest.users.model.Role;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PersonalMapperTest {
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private PersonalMapper personalMapper;
    User user1 = new User(1L, "TEST-1", "56789125E", "prueba1@prueba.com", "prueba123", LocalDateTime.now(), LocalDateTime.now(), true, Set.of(Role.ADMIN, Role.USER));
    private final Categoria categoriaPersonal = new Categoria(1L, "PERSONAL_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Personal personal =
            Personal.builder()
                    .id(UUID.randomUUID())
                    .nombre("TEST-1")
                    .dni("56789123E")
                    .seccion(categoriaPersonal)
                    .fechaAlta(LocalDate.now())
                    .fechaBaja(null)
                    .fechaCreacion(LocalDateTime.now())
                    .fechaActualizacion(LocalDateTime.now())
                    .isActive(true)
                    .user(user1)
                    .build();

    @Test
    void personalCreate(){
        // Arrange
        PersonalCreateDto personalCreateDto = PersonalCreateDto.builder()
                .dni("12389123E")
                .nombre("Nuevo_Personal")
                .seccion(categoriaPersonal.getNameCategory())
                .isActive(true)
                .build();
        UUID id = UUID.randomUUID();

        // Act
        Personal nuevoPersonal = personalMapper.toPersonalCreate(id, categoriaPersonal, personalCreateDto, user1);

        // Assert
        assertAll(
                () -> assertNotNull(nuevoPersonal),
                () -> assertEquals(id,nuevoPersonal.getId()),
                () -> assertEquals(personalCreateDto.dni(), nuevoPersonal.getDni()),
                () -> assertEquals(personalCreateDto.nombre(), nuevoPersonal.getNombre()),
                () -> assertEquals(personalCreateDto.seccion(), nuevoPersonal.getSeccion().getNameCategory()),
                () -> assertEquals(personalCreateDto.isActive(), nuevoPersonal.isActive()),
                () -> assertNotNull(nuevoPersonal.getFechaAlta()),
                () -> assertNotNull(nuevoPersonal.getFechaCreacion()),
                () -> assertNotNull(nuevoPersonal.getFechaActualizacion()),
                () -> assertNull(nuevoPersonal.getFechaBaja())
        );
    }

    @Test
    void personalUpdate(){
        // Arrange
        Personal personalExistente = personal;
        PersonalUpdateDto personalUpdateDto = PersonalUpdateDto.builder()
                .nombre("personal_actualizado")
                .seccion(categoriaPersonal.getNameCategory())
                .isActive(false)
                .build();

        // Act
        Personal personalActualizado = personalMapper.toPersonalUpdate(personalUpdateDto, personal, categoriaPersonal);

        // Assert
        assertAll(
                () -> assertNotNull(personalActualizado),
                () -> assertEquals(personalUpdateDto.nombre(), personalActualizado.getNombre()),
                () -> assertEquals(personalUpdateDto.seccion(), personalActualizado.getSeccion().getNameCategory()),
                () -> assertEquals(personalUpdateDto.isActive(), personalActualizado.isActive()),
                () -> assertEquals(personalExistente.getId(), personalActualizado.getId()),
                () -> assertEquals(personalExistente.getDni(), personalActualizado.getDni()),
                () -> assertEquals(personalExistente.getFechaCreacion(), personalActualizado.getFechaCreacion()),
                () -> assertEquals(personalExistente.getFechaAlta(), personalActualizado.getFechaAlta()),
                () -> assertEquals(personalExistente.getFechaBaja(), personalActualizado.getFechaBaja()),
                () -> assertNotNull(personalActualizado.getFechaActualizacion())
        );
    }

    @Test
    void toResponseDto(){
        // Arrange
        Personal personalExistente = personal;

        // Act
        PersonalResponseDto personalResponseDto = personalMapper.toResponseDto(personalExistente);

        // Assert
        assertAll(
                () -> assertNotNull(personalResponseDto),
                () -> assertEquals(personalExistente.getNombre(), personalResponseDto.nombre()),
                () -> assertEquals(personalExistente.getSeccion().getNameCategory(), personalResponseDto.seccion()),
                () -> assertEquals(personalExistente.isActive(), personalResponseDto.isActive()),
                () -> assertEquals(personalExistente.getId(), personalResponseDto.id()),
                () -> assertEquals(personalExistente.getId(), personalResponseDto.id()),
                () -> assertEquals(personalExistente.getFechaAlta().toString(), personalResponseDto.fechaAlta())
        );
    }

    @Test
    void toPageResponse() {
        // Arrange
        Page<Personal> page = new PageImpl<>(List.of(personal));

        // Act
        Page<PersonalResponseDto> responsePageDto = personalMapper.toPageResponse(page);

        // Assert
        assertAll(
                () -> assertNotNull(responsePageDto),
                () -> assertEquals(1, responsePageDto.getTotalElements())
        );
    }

}




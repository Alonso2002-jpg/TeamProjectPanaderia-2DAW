package org.develop.TeamProjectPanaderia.Personal.Services;

import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaNotFoundException;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.rest.personal.exceptions.PersonalBadRequest;
import org.develop.TeamProjectPanaderia.rest.personal.exceptions.PersonalBadUuid;
import org.develop.TeamProjectPanaderia.rest.personal.exceptions.PersonalNotFoundException;
import org.develop.TeamProjectPanaderia.rest.personal.exceptions.PersonalNotSaved;
import org.develop.TeamProjectPanaderia.rest.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.rest.personal.models.Personal;
import org.develop.TeamProjectPanaderia.rest.personal.repositories.PersonalRepository;
import org.develop.TeamProjectPanaderia.rest.personal.services.PersonalServiceImpl;

import static org.mockito.ArgumentMatchers.notNull;

import org.develop.TeamProjectPanaderia.rest.users.mapper.UserMapper;
import org.develop.TeamProjectPanaderia.rest.users.model.Role;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.develop.TeamProjectPanaderia.rest.users.repositories.UserRepository;
import org.develop.TeamProjectPanaderia.storage.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalServiceImplTest {
    private final Categoria categoriaPersonal = new Categoria(1L, "PERSONAL_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Personal personal1 = Personal.builder()
            .id(UUID.randomUUID())
            .nombre("PERSONAL_TEST1")
            .dni("12345678A")
            .fechaAlta(LocalDate.now())
            .fechaBaja(LocalDate.now())
            .seccion(categoriaPersonal)
            .build();
    private final Personal personal2 = Personal.builder()
            .id(UUID.randomUUID())
            .nombre("PERSONAL_TEST2")
            .dni("12345678B")
            .fechaAlta(LocalDate.now())
            .fechaBaja(LocalDate.now())
            .seccion(categoriaPersonal)
            .build();
    private final User user1 = new User(1L, "TEST-1", "56789125E", "prueba1@prueba.com", "prueba123", LocalDateTime.now(), LocalDateTime.now(), true, Set.of(Role.ADMIN, Role.USER));
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PersonalRepository personalRepository;
    @Mock
    private StorageService storageService;
    @Mock
    private CategoriaService categoriaService;
    @Mock
    private Categoria categoria;
    @Mock
    private PersonalMapper personalMapper;
    @InjectMocks
    private PersonalServiceImpl personalService;


    @Test
    void findAll_ByNombre() {
        // Arrange
        List<Personal> expectedPersonal = List.of(personal1, personal2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Personal> expectedPage = new PageImpl<>(expectedPersonal, pageable, expectedPersonal.size());
        String nombre = "PERSONAL_TEST1";

        when(personalRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        // Act
        Page<Personal> actualPage = personalService.findAll(Optional.of(nombre), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements()),
                () -> assertEquals(expectedPage.getContent(), actualPage.getContent())
        );

        // Verify
        verify(personalRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void findAll_ByDni() {
        // Arrange
        List<Personal> expectedPersonal = List.of(personal1, personal2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Personal> expectedPage = new PageImpl<>(expectedPersonal, pageable, expectedPersonal.size());
        String dni = "12345678A";

        when(personalRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        // Act
        Page<Personal> actualPage = personalService.findAll(Optional.empty(), Optional.of(dni), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements()),
                () -> assertEquals(expectedPage.getContent(), actualPage.getContent())
        );

        // Verify
        verify(personalRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void findAll_ByCategoria() {
        // Arrange
        List<Personal> expectedPersonal = List.of(personal1, personal2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Personal> expectedPage = new PageImpl<>(expectedPersonal, pageable, expectedPersonal.size());
        String categoria = "PERSONAL_TEST1";

        when(personalRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        // Act
        Page<Personal> actualPage = personalService.findAll(Optional.empty(), Optional.empty(), Optional.of(categoria), Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements()),
                () -> assertEquals(expectedPage.getContent(), actualPage.getContent())
        );

        // Verify
        verify(personalRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void findAll_ByIsActivo() {
        // Arrange
        List<Personal> expectedPersonal = List.of(personal1, personal2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Personal> expectedPage = new PageImpl<>(expectedPersonal, pageable, expectedPersonal.size());
        Boolean isActivo = true;

        when(personalRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        // Act
        Page<Personal> actualPage = personalService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(isActivo), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements()),
                () -> assertEquals(expectedPage.getContent(), actualPage.getContent())
        );

        // Verify
        verify(personalRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void findAll_ByNombreAndDni() {
        // Arrange
        List<Personal> expectedPersonal = List.of(personal1, personal2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Personal> expectedPage = new PageImpl<>(expectedPersonal, pageable, expectedPersonal.size());
        String nombre = "PERSONAL_TEST1";
        String dni = "12345678A";

        when(personalRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        // Act
        Page<Personal> actualPage = personalService.findAll(Optional.of(nombre), Optional.of(dni), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements()),
                () -> assertEquals(expectedPage.getContent(), actualPage.getContent())
        );

        // Verify
        verify(personalRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void findAll_ByNombreAndCategoria() {
        // Arrange
        List<Personal> expectedPersonal = List.of(personal1, personal2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Personal> expectedPage = new PageImpl<>(expectedPersonal, pageable, expectedPersonal.size());
        String nombre = "PERSONAL_TEST1";
        String categoria = "PERSONAL_TEST1";

        when(personalRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        // Act
        Page<Personal> actualPage = personalService.findAll(Optional.of(nombre), Optional.empty(), Optional.of(categoria), Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements()),
                () -> assertEquals(expectedPage.getContent(), actualPage.getContent())
        );

        // Verify
        verify(personalRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void findAll_ByNombreAndIsActivo() {
        // Arrange
        List<Personal> expectedPersonal = List.of(personal1, personal2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Personal> expectedPage = new PageImpl<>(expectedPersonal, pageable, expectedPersonal.size());
        String nombre = "PERSONAL_TEST1";
        Boolean isActivo = true;

        when(personalRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        // Act
        Page<Personal> actualPage = personalService.findAll(Optional.of(nombre), Optional.empty(), Optional.empty(), Optional.of(isActivo), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements()),
                () -> assertEquals(expectedPage.getContent(), actualPage.getContent())
        );

        // Verify
        verify(personalRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void findById_IdExist() {
        // Arrange
        UUID uuid = personal1.getId();
        Personal expectedProduct = personal1;
        when(personalRepository.findById(uuid)).thenReturn(Optional.of(expectedProduct));

        // Act
        Personal actualPersonal = personalService.findById(uuid.toString());

        // Assert
        assertEquals(expectedProduct, actualPersonal);


        // Verify
        verify(personalRepository, times(1)).findById(uuid);
    }

    @Test
    void findById_IdNotExist() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(personalRepository.findById(uuid)).thenReturn(Optional.empty());

        // Act Assert
        PersonalNotFoundException exception = assertThrows(
                PersonalNotFoundException.class,
                () -> personalService.findById(uuid.toString())
        );

        // Assert
        assertEquals("Personal con id " + uuid + " no encontrado", exception.getMessage());

        // Verify
        verify(personalRepository).findById(uuid);
    }

    @Test
    void savePersonal_Success() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        PersonalCreateDto personalCreateDto = new PersonalCreateDto("123456789A","Kevin Bermudez",categoriaPersonal.getNameCategory(), "prueba@prueba.com", true);
        Personal expectedPersonal = Personal.builder()
                .id(uuid)
                .nombre("nuevo_personal")
                .dni("12345678A")
                .seccion(categoriaPersonal)
                .build();

        when(categoriaService.findByName(personalCreateDto.seccion())).thenReturn(categoriaPersonal);
        when(personalRepository.findByDniEqualsIgnoreCase(any(String.class))).thenReturn(Optional.empty());
        when(userMapper.toUserFromPersonal(eq(personalCreateDto), any(String.class))).thenReturn(user1);
        when(userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(user1)).thenReturn(user1);
        when(personalMapper.toPersonalCreate(any(UUID.class), any(Categoria.class), eq(personalCreateDto), eq(user1))).thenReturn(expectedPersonal);
        when(personalRepository.save(expectedPersonal)).thenReturn(expectedPersonal);

        // Act
        Personal actualPersonal = personalService.save(personalCreateDto);

        // Assert
        assertEquals(expectedPersonal, actualPersonal);

        // Verify
        verify(categoriaService, times(1)).findByName(personalCreateDto.seccion());
        verify(personalRepository, times(1)).save(expectedPersonal);
        verify(personalRepository, times(1)).findByDniEqualsIgnoreCase(any(String.class));
        verify(personalMapper, times(1)).toPersonalCreate(any(UUID.class), eq(categoriaPersonal), eq(personalCreateDto), any(User.class));
    }

    @Test
    public void save_WhenPersonalExist() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        PersonalCreateDto personalCreateDto = new PersonalCreateDto("123456789y","Kevin Bermudez", "prueba@prueba.com", categoriaPersonal.getNameCategory(), true);
        Personal expectedPersonal = Personal.builder()
                .id(uuid)
                .nombre("nuevo_personal")
                .dni("12345678A")
                .seccion(categoriaPersonal)
                .build();

        when(categoriaService.findByName(any(String.class))).thenReturn(categoriaPersonal);
        when(userMapper.toUserFromPersonal(eq(personalCreateDto), any(String.class))).thenReturn(user1);
        when(personalRepository.findByDniEqualsIgnoreCase(any(String.class))).thenReturn(Optional.of(expectedPersonal));


        // Act Assert
        PersonalNotSaved exception = assertThrows(
                PersonalNotSaved.class,
                () -> personalService.save(personalCreateDto)
        );

        // Assert
        assertEquals("El dni " + personalCreateDto.dni() + " ya esta registrado en la BD ", exception.getMessage());

        // Verify
        verify(personalRepository, times(1)).findByDniEqualsIgnoreCase(any(String.class));
    }
    @Test
    void  save_PersonalNameAlreadyExist(){
        // Arrange
        UUID uuid = UUID.randomUUID();
        PersonalCreateDto personalCreateDto = new PersonalCreateDto("123456789y","Kevin Bermudez", "prueba@prueba.com" ,categoriaPersonal.getNameCategory(),true);
        Personal expectedPersonal = Personal.builder()
                .id(uuid)
                .nombre("nuevo_personal")
                .dni("12345678A")
                .seccion(categoriaPersonal)
                .build();

        when(categoriaService.findByName(any(String.class))).thenReturn(categoriaPersonal);
        when(userMapper.toUserFromPersonal(eq(personalCreateDto), any(String.class))).thenReturn(user1);
        when(personalRepository.findByDniEqualsIgnoreCase(any(String.class))).thenReturn(Optional.of(personal2));

        // Act Assert
        PersonalNotSaved exception = assertThrows(
                PersonalNotSaved.class,
                () -> personalService.save(personalCreateDto)
        );

        // Assert
        assertEquals("El dni " + personalCreateDto.dni() + " ya esta registrado en la BD ", exception.getMessage());

        // Verify
        verify(personalRepository, times(1)).findByDniEqualsIgnoreCase(any(String.class));
    }

    @Test
    void  save_CategoryNotExist(){
        // Arrange
        UUID uuid = UUID.randomUUID();
        PersonalCreateDto personalCreateDto = new PersonalCreateDto("123456789y","Kevin", "prueba@prueba.com" ,categoriaPersonal.getNameCategory(),true);

        when(categoriaService.findByName(personalCreateDto.seccion())).thenThrow(new CategoriaNotFoundException(personalCreateDto.seccion()));

        // Act Assert
        PersonalBadRequest exception = assertThrows(
                PersonalBadRequest.class,
                () -> personalService.save(personalCreateDto)
        );

        // Assert
        assertEquals("La categoria con nombre " + personalCreateDto.seccion() + " no existe", exception.getMessage());

        // Verify
        verify(categoriaService, times(1)).findByName(personalCreateDto.seccion());

    }


    @Test
    void savePersonal_WithDuplicatedDni() {
        // Arrange
        PersonalCreateDto personalCreateDto = new PersonalCreateDto("12345678Z", "Nombre Completo", "CATEGORIA", categoriaPersonal.getNameCategory(), true);
        Personal existingPersonal = Personal.builder().dni("12345678Z").build();

        when(personalRepository.findByDniEqualsIgnoreCase("12345678Z")).thenReturn(Optional.of(existingPersonal));
        when(categoriaService.findByName(any(String.class))).thenReturn(categoriaPersonal);
        when(userMapper.toUserFromPersonal(eq(personalCreateDto), any(String.class))).thenReturn(user1);

        // Act & Assert
        assertThrows(PersonalNotSaved.class, () -> personalService.save(personalCreateDto));

        // Verify
        verify(personalRepository).findByDniEqualsIgnoreCase("12345678Z");
    }


    @Test
    void updatePersonal_IdNotExist() {
        // Arrange
        String id = UUID.randomUUID().toString();
        PersonalUpdateDto personalDto = new PersonalUpdateDto("bun","GERENTE",true);

        when(personalRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PersonalNotFoundException.class, () -> personalService.update(id, personalDto));

        // Verify
        verify(personalRepository).findById(UUID.fromString(id));
    }

    @Test
    void updatePersonal_Success() {
        // Arrange
        UUID id = UUID.randomUUID();
        PersonalUpdateDto personalDto = new PersonalUpdateDto("Bin", "PERSONAL_TEST", true);
        Personal existingPersonal = Personal.builder()
                .id(id)
                .nombre("Nombre existente")
                .seccion(categoriaPersonal)
                .isActive(true)
                .build();

        when(personalRepository.findById(id)).thenReturn(Optional.of(existingPersonal));
        when(categoriaService.findByName(personalDto.seccion())).thenReturn(categoria);
        when(personalMapper.toPersonalUpdate(personalDto,existingPersonal,categoria)).thenReturn(existingPersonal);
        when(personalRepository.save(existingPersonal)).thenReturn(existingPersonal);

        // Act
        Personal actualPersonal = personalService.update(id.toString(), personalDto);

        // Assert
        assertNotNull(actualPersonal);

        // Verify
        verify(personalRepository).findById(id);
        verify(personalMapper, times(1)).toPersonalUpdate(personalDto, existingPersonal, categoria);
        verify(categoriaService).findByName(personalDto.seccion());
        verify(personalRepository).save(existingPersonal);
    }


    @Test
    void updatePersonal_WithAllFieldChanges() {
        // Arrange
        UUID id = UUID.randomUUID();
        PersonalUpdateDto personalDto = new PersonalUpdateDto("PEPE", "SUPERVISOR", false);
        Personal existingPersonal = Personal.builder().id(id).nombre("Manuel").isActive(true).build();
        Categoria nuevaCategoria = Categoria.builder().nameCategory("SUPERVISOR").build();

        when(personalRepository.findById(id)).thenReturn(Optional.of(existingPersonal));
        when(categoriaService.findByName("SUPERVISOR")).thenReturn(nuevaCategoria);
        when(personalMapper.toPersonalUpdate(personalDto,existingPersonal,nuevaCategoria)).thenReturn(existingPersonal);
        when(personalRepository.save(existingPersonal)).thenReturn(existingPersonal);

        // Act
        Personal actualPersonal = personalService.update(id.toString(), personalDto);

        // Assert
        assertNotNull(actualPersonal);

        // Verify
        verify(personalRepository).findById(id);
        verify(personalMapper, times(1)).toPersonalUpdate(personalDto, existingPersonal, nuevaCategoria);
        verify(categoriaService).findByName(personalDto.seccion());
        verify(personalRepository).save(existingPersonal);
    }


    @Test
    void updatePersonal_WithInvalidCategory() {
        // Arrange
        UUID id = UUID.randomUUID();
        PersonalUpdateDto personalDto = new PersonalUpdateDto("Nombre", "CATEGORIA_INVALIDA", true);
        Personal existingPersonal = Personal.builder().id(id).build();

        when(personalRepository.findById(id)).thenReturn(Optional.of(existingPersonal));
        when(categoriaService.findByName("CATEGORIA_INVALIDA")).thenThrow(new CategoriaNotFoundException("CATEGORIA_INVALIDA"));


        // Act & Assert
        PersonalBadRequest personalBadRequest = assertThrows(PersonalBadRequest.class, () -> personalService.update(id.toString(), personalDto));
        assertEquals("La categoria con nombre CATEGORIA_INVALIDA no existe", personalBadRequest.getMessage());

        // Verify
        verify(personalRepository).findById(id);
        verify(categoriaService).findByName("CATEGORIA_INVALIDA");
    }


    @Test
    void findPersonalByDni_Success() {
        // Arrange
        String dni = "12345678A";
        Personal expectedPersonal = Personal.builder()
                .id(UUID.randomUUID())
                .nombre("PERSONAL_TEST1")
                .dni(dni)
                .seccion(categoriaPersonal)
                .build();

        when(personalRepository.findByDniEqualsIgnoreCase(dni)).thenReturn(Optional.of(expectedPersonal));

        // Act
        Personal result = personalService.findPersonalByDni(dni);

        // Assert
        assertEquals(expectedPersonal, result);
    }
    @Test
    void findPersonalByDni_NotFound() {
        // Arrange
        String dni = "12345678A";
        when(personalRepository.findByDniEqualsIgnoreCase(dni)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PersonalNotFoundException.class, () -> personalService.findPersonalByDni(dni));
    }
    @Test
    void deleteById_Success() {
        // Arrange
        String id = UUID.randomUUID().toString();
        Personal personal = Personal.builder()
                .id(UUID.fromString(id))
                .nombre("pepito")
                .seccion(categoriaPersonal)
                .build();
        when(personalRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(personal));
        doNothing().when(personalRepository).delete(personal);

        // Act
        personalService.deleteById(id);

        // Assert
        verify(personalRepository).delete(personal);
    }
    @Test
    void deleteById_NotFound() {
        // Arrange
        String id = UUID.randomUUID().toString();
        when(personalRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PersonalNotFoundException.class, () -> personalService.deleteById(id));
    }
    @Test
    void findByActiveIs_Success() {
        // Arrange
        Boolean isActive = true;
        List<Personal> expectedList = Arrays.asList(new Personal(), new Personal());
        when(personalRepository.findByIsActive(isActive)).thenReturn(expectedList);

        // Act
        List<Personal> resultList = personalService.findByActiveIs(isActive);

        // Assert
        assertEquals(expectedList, resultList);
    }
    @Test
    void findByActiveIs_EmptyList() {
        // Arrange
        Boolean isActive = true;
        when(personalRepository.findByIsActive(isActive)).thenReturn(List.of());

        // Act
        List<Personal> resultList = personalService.findByActiveIs(isActive);

        // Assert
        assertTrue(resultList.isEmpty());
    }
    @Test
    void findByActiveIs_NotFound() {
        // Arrange
        Boolean isActive = true;
        when(personalRepository.findByIsActive(isActive)).thenReturn(Collections.emptyList());

        // Act
        List<Personal> result = personalService.findByActiveIs(isActive);

        // Assert
        assertTrue(result.isEmpty());
    }
    @Test
    void findPersonalByDni_NotFound_ThrowsException() {
        // Arrange
        String dni = "12345678A";
        when(personalRepository.findByDniEqualsIgnoreCase(dni)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PersonalNotFoundException.class, () -> personalService.findPersonalByDni(dni));
        verify(personalRepository).findByDniEqualsIgnoreCase(dni); // Verifica que se llamó al método correctamente
    }
    @Test
    void findPersonalByDni_Found_Success() {
        // Arrange
        String dni = "12345678A";
        Personal expectedPersonal = Personal.builder()
                .id(UUID.randomUUID())
                .nombre("PERSONAL_TEST1")
                .dni(dni)
                .seccion(categoriaPersonal)
                .build();
        when(personalRepository.findByDniEqualsIgnoreCase(dni)).thenReturn(Optional.of(expectedPersonal));

        // Act
        Personal result = personalService.findPersonalByDni(dni);

        // Assert
        assertEquals(expectedPersonal, result);
        verify(personalRepository).findByDniEqualsIgnoreCase(dni); // Verifica que se llamó al método correctamente
    }
    @Test
    void findById_ValidId_NotFound_ThrowsException() {
        // Arrange
        String id = UUID.randomUUID().toString();
        when(personalRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PersonalNotFoundException.class, () -> personalService.findById(id));
        verify(personalRepository).findById(UUID.fromString(id));
    }
/*
    @Test
    void updatePersonal_CategoryNotFound() {
        // Arrange
        String id = UUID.randomUUID().toString();
        PersonalUpdateDto personalDto = new PersonalUpdateDto("Actualizado", "CATEGORIA_INEXISTENTE", true);
        Personal personalUpd = Personal.builder()
                .id(UUID.fromString(id))
                .seccion(categoriaPersonal)
                .isActive(true)
                .nombre("tetito")
                .build(); // Método ficticio para crear un Personal existente

        when(personalRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(personalUpd));
        when(categoriaService.findByName(personalDto.seccion())).thenReturn(null);

        // Act & Assert
        assertThrows(PersonalNotSaved.class, () -> personalService.update(id, personalDto));

        // Verify
        verify(personalRepository).findById(UUID.fromString(id));
        verify(categoriaService).findByName(personalDto.seccion());
        verify(personalRepository, never()).save(any(Personal.class));
    }

    @Test
    void updatePersonal_InvalidName() {
        // Arrange
        String id = UUID.randomUUID().toString();
        PersonalUpdateDto personalDto = new PersonalUpdateDto("", "CATEGORIA_EXISTENTE", true); // Nombre inválido
        Personal personalUpd = Personal.builder()
                .id(UUID.fromString(id))
                .nombre("Nombre existente")
                .seccion(categoriaPersonal)
                .isActive(true)
                .build();

        when(personalRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(personalUpd));
        when(categoriaService.findByName(personalDto.seccion())).thenReturn(new Categoria());
        when(personalRepository.save(any(Personal.class))).thenReturn(personalUpd);

        // Act
        Personal actualPersonal = personalService.update(id, personalDto);

        // Assert
        assertNotNull(actualPersonal);
        assertNotEquals("", actualPersonal.getNombre());

        // Verify
        verify(personalRepository).findById(UUID.fromString(id));
        verify(categoriaService).findByName(personalDto.seccion());
        verify(personalRepository).save(personalUpd);
    }
    */
    @Test
    void findById_Success() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Personal expectedPersonal = new Personal(); // Configura tu objeto Personal aquí
        when(personalRepository.findById(uuid)).thenReturn(Optional.of(expectedPersonal));

        // Act
        Personal foundPersonal = personalService.findById(uuid.toString());

        // Assert
        assertEquals(expectedPersonal, foundPersonal);
    }

    @Test
    void findById_NotFound() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(personalRepository.findById(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PersonalNotFoundException.class, () -> personalService.findById(uuid.toString()));
    }

    @Test
    void findById_InvalidUuidFormat() {
        // Arrange
        String invalidUuid = "123";

        // Act & Assert
        assertThrows(PersonalBadUuid.class, () -> personalService.findById(invalidUuid));
    }
    @Test
    void findAll_WhenNombreIsPresent() {
        // Arrange
        String nombre = "NombreTest";
        Pageable pageable = PageRequest.of(0, 10);
        List<Personal> expectedPersonnel = List.of(personal1, personal2);
        Page<Personal> expectedPage = new PageImpl<>(expectedPersonnel, pageable, expectedPersonnel.size());

        when(personalRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        // Act
        Page<Personal> result = personalService.findAll(Optional.of(nombre), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertFalse(result.getContent().isEmpty());
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());

        // Verify
        verify(personalRepository).findAll(any(Specification.class), eq(pageable));
    }


    @Test
    void findAll_WhenDniDoesNotMatch() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(personalRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        // Act
        Page<Personal> result = personalService.findAll(Optional.empty(), Optional.of("NonExistingDni"), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertTrue(result.getContent().isEmpty());
        // Verifica que se utilizó el criterio por defecto cuando el DNI no coincide
        verify(personalRepository).findAll(any(Specification.class), eq(pageable));
    }
    @Test
    void findAll_BySeccion() {
        // Arrange
        Optional<String> seccion = Optional.of("SECCION_TEST");
        List<Personal> expectedPersonnel = List.of(personal1, personal2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Personal> expectedPage = new PageImpl<>(expectedPersonnel);

        when(personalRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Personal> actualPage = personalService.findAll(Optional.empty(), Optional.empty(), seccion, Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(personalRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }


}


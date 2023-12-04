package org.develop.TeamProjectPanaderia.rest.personal.mapper;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalResponseDto;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.rest.personal.models.Personal;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserInfoResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.mapper.UserMapper;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Clase encargada de realizar la conversión entre objetos relacionados con la entidad Personal.
 * Transforma objetos DTO (Data Transfer Object) y entidades de base de datos relacionadas con Personal.
 *
 * @Component Indica que esta clase debe ser escaneada automáticamente y gestionada como un componente por Spring.
 */
@Component
public class PersonalMapper {
    private final UserMapper userMapper;

    /**
     * Constructor que recibe una instancia de UserMapper.
     *
     * @param userMapper Instancia de UserMapper utilizada para la conversión de objetos relacionados con User.
     * @Autowired Anotación utilizada para la inyección de dependencias automática por Spring.
     */
    @Autowired
    public PersonalMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * Convierte un objeto PersonalCreateDto a una entidad Personal.
     *
     * @param id        Identificador único del trabajador.
     * @param categoria Categoría del trabajador.
     * @param dto       Objeto PersonalCreateDto que contiene la información del trabajador a crear.
     * @param user      Objeto User relacionado con el trabajador.
     * @return Objeto Personal creado a partir de los parámetros proporcionados.
     */
    public Personal toPersonalCreate(UUID id, Categoria categoria, PersonalCreateDto dto, User user) {
        return Personal.builder()
                .id(id)
                .dni(dto.dni())
                .nombre(dto.nombre())
                .seccion(categoria)
                .isActive(dto.isActive() != null ? dto.isActive() : true)
                .fechaAlta(LocalDate.now())
                .fechaBaja(null)
                .email(dto.email())
                .user(user)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();
    }

    /**
     * Convierte un objeto PersonalUpdateDto y una entidad Personal existente a una nueva entidad Personal actualizada.
     *
     * @param personalDto Objeto PersonalUpdateDto que contiene la información actualizada del trabajador.
     * @param personalUpd Entidad Personal existente que se va a actualizar.
     * @param categoria   Categoría del trabajador.
     * @return Nueva entidad Personal con la información actualizada.
     */
    public Personal toPersonalUpdate(PersonalUpdateDto personalDto, Personal personalUpd, Categoria categoria) {
        return Personal.builder()
                .id(personalUpd.getId())
                .dni(personalUpd.getDni())
                .nombre(personalDto.nombre() == null ? personalUpd.getNombre() : personalDto.nombre())
                .seccion(categoria)
                .fechaAlta(personalUpd.getFechaAlta())
                .fechaBaja(personalUpd.getFechaBaja())
                .fechaCreacion(personalUpd.getFechaCreacion())
                .fechaActualizacion(LocalDateTime.now())
                .isActive(personalDto.isActive() == null ? personalUpd.isActive() : personalDto.isActive())
                .build();
    }


    /**
     * Convierte una entidad Personal a un objeto PersonalResponseDto para enviar como respuesta.
     *
     * @param personal Entidad Personal que se va a convertir.
     * @return Objeto PersonalResponseDto con la información del trabajador para enviar como respuesta.
     */
    public PersonalResponseDto toResponseDto(Personal personal) {
        return  new PersonalResponseDto(
                personal.getId(),
                personal.getDni(),
                personal.getNombre(),
                personal.getSeccion().getNameCategory(),
                personal.getFechaAlta().toString(),
                personal.isActive(),
                personal.getUser().getId()
        );
    }

    /**
     * Convierte una lista de entidades Personal a una lista de objetos PersonalResponseDto.
     *
     * @param personals Lista de entidades Personal que se van a convertir.
     * @return Lista de objetos PersonalResponseDto con la información de los trabajadores.
     */
    public List<PersonalResponseDto> toResponseList(List<Personal> personals){
        return personals.stream()
                .map(this::toResponseDto)
                .toList();
    }

    /**
     * Convierte una página de entidades Personal a una página de objetos PersonalResponseDto.
     *
     * @param personalPages Página de entidades Personal que se va a convertir.
     * @return Página de objetos PersonalResponseDto con la información de los trabajadores.
     */
    public Page<PersonalResponseDto> toPageResponse(Page<Personal> personalPages){
        return personalPages.map(this::toResponseDto);
    }
}
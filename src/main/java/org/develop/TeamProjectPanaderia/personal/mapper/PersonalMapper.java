package org.develop.TeamProjectPanaderia.personal.mapper;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalResponseDto;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.personal.models.Personal;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class PersonalMapper {
    public Personal toPersonalCreate(UUID id, Categoria categoria, PersonalCreateDto dto) {
        return Personal.builder()
                .id(id)
                .dni(dto.dni())
                .nombre(dto.nombre())
                .seccion(categoria)
                .isActive(dto.isActive() != null ? dto.isActive() : true)
                .fechaAlta(LocalDate.now())
                .fechaBaja(null)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();
    }

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

    public PersonalResponseDto toResponseDto(Personal personal) {
        return  new PersonalResponseDto(
                personal.getId(),
                personal.getDni(),
                personal.getNombre(),
                personal.getSeccion().getNameCategory(),
                personal.getFechaAlta().toString(),
                personal.isActive()
        );
    }

    public List<PersonalResponseDto> toResponseList(List<Personal> personals){
        return personals.stream()
                .map(this::toResponseDto)
                .toList();
    }

    public Page<PersonalResponseDto> toPageResponse(Page<Personal> personalPages){
        return personalPages.map(this::toResponseDto);
    }
}
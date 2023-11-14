package org.develop.TeamProjectPanaderia.personal.mapper;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalResponseDto;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.personal.models.Personal;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class PersonalMapper {
    public Personal toPersonal(UUID id, Categoria categoria, PersonalCreateDto dto) {
        return Personal.builder()
                .dni(dto.dni())
                .isActive(dto.isActive())
                .build();
    }

    public PersonalResponseDto toResponse(Personal personal) {
        return  new PersonalResponseDto(
                personal.getDni(),
                personal.getNombre(),
                personal.getSeccion().getNameCategory(),
                personal.getFechaAlta().toString(),
                personal.getFechaBaja().toString(),
                personal.getFechaCreacion().toString(),
                personal.getFechaUpdate().toString(),
                personal.isActive()

        );
    }
    public  Personal toPersonalCreate(PersonalCreateDto personalDto, Categoria categoria){
        return Personal.builder()
                .dni(personalDto.dni())
                .nombre(personalDto.name())
                .seccion(categoria)
                .isActive(personalDto.isActive())
                .build();
    }





    public List<PersonalResponseDto> toResponseList(List<Personal> personals){
        return personals.stream()
                .map(this::toResponse)
                .toList();
    }

    public Personal toPersonalUdate(PersonalUpdateDto personalDto, Personal personalUpd, Categoria categoria) {
        return Personal.builder()
                .dni(personalUpd.getDni())
                .nombre(personalDto.nombre() == null ? personalUpd.getNombre() : personalDto.nombre())
                .seccion(categoria == null ? personalUpd.getSeccion() : categoria)
                .fechaAlta(personalUpd.getFechaAlta())
                .fechaBaja(personalDto.fechaBaja() == null ? personalUpd.getFechaBaja() : personalDto.fechaBaja())
                .isActive(personalDto.isActive() == null ? personalUpd.isActive() : personalDto.isActive())
                .build();
    }

}

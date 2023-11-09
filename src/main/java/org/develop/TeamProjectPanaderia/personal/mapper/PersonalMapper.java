package org.develop.TeamProjectPanaderia.personal.mapper;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.personal.dto.CreateResponseDto;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.personal.models.Personal;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static org.hibernate.engine.transaction.internal.jta.JtaStatusHelper.isActive;
@Component
public class PersonalMapper {
    public Personal toPersonal(PersonalCreateDto dto) {
        return Personal.builder()
                .dni(dto.dni())
                .isActive(dto.isActive())
                .build();
    }
    public CreateResponseDto toPersonalCreateDto(Personal personal) {
        return  CreateResponseDto.builder()
                .dni(personal.getDni())
                .name(personal.getNombre())
                .seccion(personal.getSeccion().getNameCategory())
                .fechaAlta(personal.getFechaAlta().toString())
                .fechaBaja(personal.getFechaBaja().toString())
                .fechaCreacion(personal.getFechaCreacion().toString())
                .fechaUpdate(personal.getFechaUpdate().toString())
                .isActive(personal.isActive())
                .build();
    }
      public PersonalUpdateDto toPersonalUpdateDto(Personal personal) {
        return PersonalUpdateDto.builder()
                .nombre(personal.getNombre())
                .section(personal.getSeccion().getNameCategory())
                .fechaBaja(LocalDate.parse(personal.getFechaBaja().toString()))
                .isActive(personal.isActive())
                .build();
        }
        public Personal toPersonalUpdate(PersonalUpdateDto dto, Personal personal) {
            return Personal.builder()
                    .nombre(dto.nombre())
                    .seccion(personal.getSeccion())
                    .fechaBaja(dto.fechaBaja())
                    .isActive(dto.isActive())
                    .build();
        }


    public List<CreateResponseDto> toResponseList(List<Personal> personals){
        return personals.stream()
                .map(this::toPersonalCreateDto)
                .toList();
    }
}

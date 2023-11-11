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

import static org.hibernate.engine.transaction.internal.jta.JtaStatusHelper.isActive;
@Component
public class PersonalMapper {
    public Personal toPersonal(PersonalCreateDto dto) {
        return Personal.builder()
                .dni(dto.dni())
                .isActive(dto.isActive())
                .build();
    }
    public PersonalCreateDto toCreate(Personal personal){
        return new PersonalCreateDto(
                personal.getDni(),
                personal.getNombre(),
                personal.getSeccion().getNameCategory(),
                personal.getFechaAlta().toString(),
                personal.getFechaBaja().toString(),
                personal.isActive()
        );
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


        public Personal toUpdate(PersonalUpdateDto dto, Personal personal, Categoria categoria) {
            return Personal.builder()
                    .nombre(dto.nombre())
                    .seccion(categoria == null ? personal.getSeccion() : categoria)
                    .fechaBaja(dto.fechaBaja() )
                    .isActive(dto.isActive())
                    .build();
        }



    public List<PersonalResponseDto> toResponseList(List<Personal> personals){
        return personals.stream()
                .map(this::toResponse)
                .toList();
    }

    public Personal toPersonal(PersonalUpdateDto personalDto, Personal personalUpd, Categoria categoria) {
        return Personal.builder()
                .dni(personalUpd.getDni())
                .nombre(personalDto.nombre() == null ? personalUpd.getNombre() : personalDto.nombre())
                .seccion(categoria == null ? personalUpd.getSeccion() : categoria)
                .fechaAlta(personalUpd.getFechaAlta())
                .fechaBaja(personalDto.fechaBaja() == null ? personalUpd.getFechaBaja() : personalDto.fechaBaja())
                .isActive(personalDto.isActive() == null ? personalUpd.isActive() : personalDto.isActive())
                .build();
    }
    public Personal toPersonal(UUID id, PersonalCreateDto createDto,Categoria categoria, Personal personal) {
        return  Personal.builder()
                .id(id)
                .nombre(createDto.name())
                .dni(createDto.dni())
                .seccion(categoria)
                .fechaCreacion(LocalDate.from(LocalDateTime.now()))
                .fechaUpdate(LocalDate.from(LocalDateTime.now()))
                .isActive(true)
                .build();

    }
}

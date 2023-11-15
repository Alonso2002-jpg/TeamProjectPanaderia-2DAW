package org.develop.TeamProjectPanaderia.rest.personal.services;

import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.rest.personal.models.Personal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
public interface PersonalService {
    Page<Personal> findAll(Optional<String> nombre, Optional <String> dni, Optional<String> seccion, Optional<Boolean> isActivo, Pageable pageable);
    Personal findById(String id);
    Personal findPersonalByDni(String dni);
    Personal save(PersonalCreateDto personal);
    Personal update(String id, PersonalUpdateDto personal);
    void deleteById(String id);
    List<Personal> findByActiveIs(Boolean isActive);

}

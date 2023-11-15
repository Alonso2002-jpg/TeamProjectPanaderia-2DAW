package org.develop.TeamProjectPanaderia.rest.personal.services;

import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.rest.personal.models.Personal;

import java.util.List;
import java.util.Optional;

public interface PersonalService {
    List<Personal>findAll(Boolean isActive, String seccion);
    Personal findById(String id);
    Personal save(PersonalCreateDto personal);
    Personal update(String id, PersonalUpdateDto personal);
    Optional<Personal> findPersonalByDni(String dni);
    void deleteById(String id);
    List<Personal> findByActiveIs(Boolean isActive);


}

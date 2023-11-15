package org.develop.TeamProjectPanaderia.personal.services;

import org.develop.TeamProjectPanaderia.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.personal.models.Personal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonalService {
    List<Personal>findAll(Boolean isActive, String seccion);
    Personal findById(String id);
    Personal save(PersonalCreateDto personal);
    Personal update(String id, PersonalUpdateDto personal);
    Optional<Personal> findPersonalByDni(String dni);
    void deleteById(String id);
    List<Personal> findByActiveIs(Boolean isActive);


}

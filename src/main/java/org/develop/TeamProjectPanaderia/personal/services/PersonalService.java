package org.develop.TeamProjectPanaderia.personal.services;

import org.develop.TeamProjectPanaderia.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.personal.models.Personal;

import java.util.List;
import java.util.UUID;

public interface PersonalService {
    List<Personal>findAll(Boolean isActive);
    Personal findById(UUID id);
    Personal save(PersonalCreateDto personal);
    Personal update(UUID id, PersonalUpdateDto personal);
    void deleteById(UUID id);
    Personal findByName(String name);
    List<Personal> findByActiveIs(boolean isActive);
    void deleteAll();

}

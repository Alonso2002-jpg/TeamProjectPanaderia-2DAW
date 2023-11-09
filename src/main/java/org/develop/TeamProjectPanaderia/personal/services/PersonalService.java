package org.develop.TeamProjectPanaderia.personal.services;

import org.develop.TeamProjectPanaderia.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.personal.models.Personal;

import java.util.List;
import java.util.UUID;

public interface PersonalService {
    List<Personal>findAll();
    Personal findById(UUID id);
    Personal save(PersonalCreateDto personal);
    Personal update(UUID id, PersonalUpdateDto personal);
    void deleteById(UUID id);
    void deleteAll();

}

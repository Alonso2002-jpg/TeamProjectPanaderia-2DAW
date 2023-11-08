package org.develop.TeamProjectPanaderia.personal.services;

import org.develop.TeamProjectPanaderia.personal.models.Personal;

import java.util.List;

public interface PersonalService {
    List<Personal>findAll();
    Personal findById(Long id);
    Personal save(Personal personal);
    Personal update(Long id,Personal personal);
    void deleteById(Long id);
    void deleteAll();

}

package org.develop.TeamProjectPanaderia.personal.services;

import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.personal.exceptions.PersonalException;
import org.develop.TeamProjectPanaderia.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.personal.models.Personal;
import org.develop.TeamProjectPanaderia.personal.repositories.PersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PersonalServiceImpl implements PersonalService {
    private final PersonalRepository personalRepository;
    private final PersonalMapper personalMapper;
    private final CategoriaService categoriaService;
    @Autowired
    public PersonalServiceImpl(PersonalRepository personalRepository, PersonalMapper personalMapper,  CategoriaService categoriaService) {
        this.personalRepository = personalRepository;
        this.personalMapper = personalMapper;
        this.categoriaService = categoriaService;
    }




    @Override
    public List<Personal> findAll(Boolean isActive) {
        return isActive != null ? personalRepository.findByIsActive(isActive) : personalRepository.findAll();
    }

    @Override
    public Personal findById(UUID id) {
        return personalRepository.findByUUID(id).orElseThrow(()-> new PersonalException("Persona no encontrada"));
    }

    @Override
    public Personal save(PersonalCreateDto personal) {
        if (personalRepository.findByUUID(UUID.fromString(personal.dni())).isPresent()){
            throw new PersonalException("Personal ya existe");
        }
        return personalRepository.save(personalMapper.toPersonal(personal));
    }

    @Override
    public Personal update(UUID id, PersonalUpdateDto personalDto) {
        var personalUpd = findById(id);
        var categoria = categoriaService.findByName(personalDto.section());
        return personalRepository.save(personalMapper.toPersonal(personalDto, personalUpd, categoria));

    }


    @Override
    public void deleteById(UUID id) {
        var personal = findById(id);
        personalRepository.delete(personal);
    }



    @Override
    public List<Personal> findByActiveIs(boolean isActive) {
        return personalRepository.findByIsActive(isActive);
    }

    @Override
    public void deleteAll() {
        personalRepository.deleteAll();

    }
}

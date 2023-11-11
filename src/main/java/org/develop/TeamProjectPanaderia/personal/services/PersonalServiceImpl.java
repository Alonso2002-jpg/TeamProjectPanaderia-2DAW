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
    private final PersonalService personalService;
    @Autowired
    public PersonalServiceImpl(PersonalRepository personalRepository, PersonalMapper personalMapper, CategoriaService categoriaService, PersonalService personalService) {
        this.personalRepository = personalRepository;
        this.personalMapper = personalMapper;
        this.categoriaService = categoriaService;
        this.personalService = personalService;
    }




    @Override
    public List<Personal> findAll(Boolean isActive, String seccion) {
        if ((seccion == null || seccion.isEmpty()) && (isActive== null || isActive )) {
            log.info("Buscando todos los personales ");
        return personalRepository.findAll();
        }
        if (seccion != null && !seccion.isEmpty() && (isActive==null || isActive)) {
            log.info("Buscando todos los personales por categoria ");
            return personalRepository.findAllByCategoriaContainsIgnoreCase(seccion);
        }
        if ( (seccion ==null || seccion.isEmpty())){
            log.info("Buscando todas las secciones por personal");
            return personalRepository.findAllByIsActiveIgnereCase(isActive);
        }
        log.info("Buscando todo todos los perosnales por seccion: "+ seccion+" y si esta activo o no: "+isActive);
        return  personalRepository.findAllByIsActiveIgnorecaseCategoryIgnoreCase( seccion ,isActive);
    }

    @Override
    public Personal findById(UUID id) {
        return personalRepository.findByUUID(id).orElseThrow(()-> new PersonalException("Persona no encontrada"));
    }

    @Override
    public Personal save(PersonalCreateDto perosnalCreateDto) {
      log.info("Guardando Personal");
      Categoria categoria= categoriaService.findByName(perosnalCreateDto.seccion());
      Personal personal = personalService.findById(UUID.fromString(perosnalCreateDto.dni()));
      UUID id = UUID.randomUUID();
      return personalRepository.save(personalMapper.toPersonal(id, perosnalCreateDto , categoria,personal));
    }

    @Override
    public Personal update(UUID id, PersonalUpdateDto personalDto) {
        log.info("Actualizando");
        var personalUpd = findById(id);
        var categoria = categoriaService.findByName(personalDto.section());
        return personalRepository.save(personalMapper.toPersonal(personalDto, personalUpd, categoria));

    }


    @Override
    public void deleteById(UUID id) {
        log.info("Borrando por id");
        var personal = findById(id);
        personalRepository.delete(personal);
    }


    @Override
    public List<Personal> findByActiveIs(boolean isActive) {
        return personalRepository.findByIsActive(isActive);
    }


}

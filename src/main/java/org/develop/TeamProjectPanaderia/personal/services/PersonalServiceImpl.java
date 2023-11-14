package org.develop.TeamProjectPanaderia.personal.services;

import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.personal.exceptions.PersonalException;
import org.develop.TeamProjectPanaderia.personal.exceptions.PersonalNotFoundException;
import org.develop.TeamProjectPanaderia.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.personal.models.Personal;
import org.develop.TeamProjectPanaderia.personal.repositories.PersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PersonalServiceImpl implements PersonalService {
    private final PersonalRepository personalRepository;
    private final PersonalMapper personalMapper;
    private final CategoriaService categoriaService;
    @Autowired
    public PersonalServiceImpl(PersonalRepository personalRepository, PersonalMapper personalMapper, CategoriaService categoriaService) {
        this.personalRepository = personalRepository;
        this.personalMapper = personalMapper;
        this.categoriaService = categoriaService;
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
    public Personal findById(String id) {
        log.info("Buscando por id");
        try {
            var uudi = UUID.fromString(id);
            return personalRepository.findByUUID(uudi).orElseThrow(() -> new PersonalNotFoundException("No se ha encontrado el personal con id: " + uudi) {
            });
        } catch (IllegalArgumentException e) {
            throw new PersonalException("El id: " + id + " no es un UUID válido");
        }
    }

    @Override
    public Personal save(PersonalCreateDto perosnalCreateDto) {
      log.info("Guardando Personal");
      Categoria categoria= categoriaService.findByName(perosnalCreateDto.seccion());
      UUID id= UUID.randomUUID();

        return personalRepository.save( personalMapper.toPersonal(id, categoria, perosnalCreateDto));
    }

    @Override
    public Personal update(String id, PersonalUpdateDto personalDto) {
        log.info("Actualizando");
        var personalUpd = this.findById(id);
        Categoria categoria = null;
        if (personalDto.section() != null && !personalDto.section().isEmpty()) {
           categoria= categoriaService.findByName(personalDto.section());
        } else {
            categoria = personalUpd.getSeccion();
        }

        return personalRepository.save(personalMapper.toPersonalUdate(personalDto, personalUpd, categoria));

    }

    @Override
    public Optional <Personal> findPersonalByDni(String dni) {
        return personalRepository.findByDni(dni);
    }


    @Override
    public void deleteById(String id) {
        log.info("Borrando por id");
        var personal = findById(id);
        personalRepository.delete(personal);
    }

    @Override
    public List<Personal> findByActiveIs(Boolean isActive) {
        return personalRepository.findByIsActive(isActive);
    }


}

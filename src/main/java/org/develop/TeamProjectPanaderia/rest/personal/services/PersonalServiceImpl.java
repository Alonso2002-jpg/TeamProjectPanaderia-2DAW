package org.develop.TeamProjectPanaderia.rest.personal.services;

import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Direccion;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserRequestDto;
import org.develop.TeamProjectPanaderia.rest.users.mapper.UserMapper;
import org.develop.TeamProjectPanaderia.rest.users.model.Role;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.develop.TeamProjectPanaderia.rest.users.repositories.UserRepository;
import org.develop.TeamProjectPanaderia.storage.services.StorageService;
import org.springframework.cache.annotation.Cacheable;
import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;

import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaNotFoundException;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.rest.personal.exceptions.PersonalBadRequest;
import org.develop.TeamProjectPanaderia.rest.personal.exceptions.PersonalBadUuid;
import org.develop.TeamProjectPanaderia.rest.personal.exceptions.PersonalNotFoundException;
import org.develop.TeamProjectPanaderia.rest.personal.exceptions.PersonalNotSaved;
import org.develop.TeamProjectPanaderia.rest.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.rest.personal.models.Personal;
import org.develop.TeamProjectPanaderia.rest.personal.repositories.PersonalRepository;
import org.develop.TeamProjectPanaderia.storage.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Service
@CacheConfig(cacheNames = "personal")

public class PersonalServiceImpl implements PersonalService {
    private final PersonalRepository personalRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PersonalMapper personalMapper;
    private final CategoriaService categoriaService;

    @Autowired
    public PersonalServiceImpl(PersonalRepository personalRepository, UserMapper userMapper,UserRepository userRepository,PersonalMapper personalMapper, CategoriaService categoriaService) {
        this.personalRepository = personalRepository;
        this.personalMapper = personalMapper;
        this.categoriaService = categoriaService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    public Page<Personal> findAll(Optional<String> nombre, Optional<String> dni, Optional<String> seccion, Optional<Boolean> isActivo, Pageable pageable) {
        Specification<Personal> specNombrePersonal = (root, query, criteriaBuilder) ->
                nombre.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> specDniPersonal = (root, query, criteriaBuilder) ->
                dni.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("dni")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> specSeccionPersonal = (root, query, criteriaBuilder) ->
                seccion.map(c -> {
                    Join<Personal, Categoria> categoriaJoin = root.join("seccion");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("nameCategory")), "%" + c.toLowerCase() + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> specIsActivo = (root, query, criteriaBuilder) ->
                isActivo.map(a -> criteriaBuilder.equal(root.get("active"), a))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> criterio = Specification.where(specNombrePersonal)
                .and(specDniPersonal)
                .and(specSeccionPersonal)
                .and(specIsActivo);
        return personalRepository.findAll(criterio, pageable);
    }

    @Override
    @Cacheable
    public Personal findById(String id) {
        log.info("Buscando por id");
        try {
            var uuid = UUID.fromString(id);
            return personalRepository.findById(uuid).orElseThrow(() -> new PersonalNotFoundException(uuid) {
            });
        } catch (IllegalArgumentException e) {
            throw new PersonalBadUuid(id);
        }
    }

    @Override
    @CachePut
    public Personal save(PersonalCreateDto personalCreateDto) {
        log.info("Guardando Personal");
        try{
            Categoria categoria = categoriaService.findByName(personalCreateDto.seccion());
            UUID id = UUID.randomUUID();
            User user = userMapper.toUserFromPersonal(personalCreateDto, getPasswordFromPersonal(personalCreateDto));
            if (personalRepository.findByDniEqualsIgnoreCase(personalCreateDto.dni()).isPresent()) {
                throw new PersonalNotSaved(personalCreateDto.dni());
            }else if (userRepository.findByUsernameIgnoreCase(personalCreateDto.dni()).isPresent()) {
                return personalRepository.save(personalMapper.toPersonalCreate(id, categoria, personalCreateDto,userRepository.findByUsernameIgnoreCase(personalCreateDto.dni()).get()));
            }
            if (userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(personalCreateDto.dni(), personalCreateDto.email()).isPresent()){
                throw new PersonalBadRequest("El Email ya esta vinculado a una cuenta");
            }
            userRepository.save(user);
            return personalRepository.save(personalMapper.toPersonalCreate(id, categoria, personalCreateDto,user));
        } catch (CategoriaNotFoundException e){
            throw new PersonalBadRequest(e.getMessage());
        }
    }

    @Override
    @CachePut
    public Personal update(String id, PersonalUpdateDto personalDto) {
        log.info("Actualizando");
        try{
            var personalUpd = this.findById(id);
            Categoria categoria = null;
            if (personalDto.seccion() != null && !personalDto.seccion().isEmpty()) {
                categoria= categoriaService.findByName(personalDto.seccion());
            } else {
                categoria = personalUpd.getSeccion();
            }
            return personalRepository.save(personalMapper.toPersonalUpdate(personalDto, personalUpd, categoria));
        } catch (CategoriaNotFoundException e){
            throw new PersonalBadRequest(personalDto.seccion());
        }
    }
    @Override
    @Cacheable
    public Personal findPersonalByDni(String dni) {
        log.info("Buscando por dni");
        return personalRepository.findByDniEqualsIgnoreCase(dni).orElseThrow(() -> new PersonalNotFoundException(dni));
    }

    @Override
    @CacheEvict
    public void deleteById(String id) {
        log.info("Borrando por id");
        var personal = findById(id);
        personalRepository.delete(personal);
    }

    @Override
    public List<Personal> findByActiveIs(Boolean isActive) {
        return personalRepository.findByIsActive(isActive);
    }


    public String getPasswordFromPersonal(PersonalCreateDto personal){
        String[] completeName = personal.nombre().split(" ");
        return completeName[0].trim().substring(0,2).toLowerCase() + completeName[1].trim() + personal.dni().substring(personal.dni().length()-4);
    }
}

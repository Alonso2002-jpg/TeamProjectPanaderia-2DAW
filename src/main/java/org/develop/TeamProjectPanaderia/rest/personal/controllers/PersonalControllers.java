package org.develop.TeamProjectPanaderia.rest.personal.controllers;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalResponseDto;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.rest.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.rest.personal.services.PersonalService;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
@RestController
@Slf4j
@RequestMapping("/personal")
public class PersonalControllers {
    private final PersonalService personalService;
    private final PersonalMapper personalMapper;

    @Autowired
    public PersonalControllers(PersonalService personalService, PersonalMapper personalMapper) {
        this.personalService = personalService;
        this.personalMapper = personalMapper;
    }
    @GetMapping
    public ResponseEntity<PageResponse<PersonalResponseDto>> getAllPersonal(
            @RequestParam(required = false) Optional<String> nombre,
            @RequestParam(required = false) Optional<String> dni,
            @RequestParam(required = false) Optional<String> categoria,
            @RequestParam(required = false) Optional<Boolean> isActivo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction){
        log.info("Se optiene el Personal por : " + nombre + ", " + dni + ", " + categoria + ", " + isActivo);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(personalMapper.toPageResponse(personalService.findAll(nombre,dni,categoria, isActivo,pageable)),sortBy,direction));
    }
    @GetMapping("/{id}")
    public ResponseEntity<PersonalResponseDto> getPersonalById(@PathVariable String id){
        log.info("Se optiene el Personal por id: " + id);
        return ResponseEntity.ok(personalMapper.toResponseDto(personalService.findById(id)));
    }
    @PostMapping
    public ResponseEntity<PersonalResponseDto>createPersonal(@RequestBody @Valid PersonalCreateDto personalCreateDto){
        log.info("Se crea el Personal: " + personalCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(personalMapper.toResponseDto(personalService.save(personalCreateDto)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<PersonalResponseDto> updatePersonal(@PathVariable String id, @RequestBody @Valid PersonalUpdateDto personalUpdateDto){
        log.info("Se actualiza el Personal: " + personalUpdateDto);
        return ResponseEntity.ok(personalMapper.toResponseDto(personalService.update(id,personalUpdateDto)));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<PersonalResponseDto> patchPersonal(@PathVariable String id, @RequestBody @Valid PersonalUpdateDto personalUpdateDto){
        log.info("Se actualiza el Personal: " + personalUpdateDto);
        return ResponseEntity.ok(personalMapper.toResponseDto(personalService.update(id,personalUpdateDto)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonal(@PathVariable String id){
        log.info("Se elimina el Personal por id: " + id);
        personalService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}

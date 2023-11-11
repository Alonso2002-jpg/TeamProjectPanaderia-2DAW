package org.develop.TeamProjectPanaderia.personal.controllers;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.personal.models.Personal;
import org.develop.TeamProjectPanaderia.personal.services.PersonalService;
import org.develop.TeamProjectPanaderia.producto.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<List<Personal>> getAllPersonal(
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String categoria){
        log.info("Buscando todo todo el perosnal con categoría");
        return ResponseEntity.ok(personalService.findAll(isActive, categoria));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Personal> getPresonalfindById(@PathVariable UUID id){
        log.info("buscando por id");
        return ResponseEntity.ok(personalService.findById(id));

    }
    @PostMapping
    public ResponseEntity<Personal> createPersonal(@PathVariable PersonalCreateDto productoCreatedto){
        log.info("create personal: "+productoCreatedto+" productoCreatedto");
        return ResponseEntity.status(HttpStatus.CREATED).body(personalService.save(productoCreatedto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Personal> updatePersonal(@PathVariable UUID id, @Valid @RequestBody PersonalUpdateDto personalUpdateteDto){
        log.info("update personal: "+ id+" producto: "+personalUpdateteDto);
        return ResponseEntity.ok(personalService.update(id,personalUpdateteDto));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Personal> updatePartialPersonal(@PathVariable UUID id,@Valid @RequestBody PersonalUpdateDto personalUpdateteDto){
        log.info("actualizando partialmente personal: "+ id+" product");
        return ResponseEntity.ok(personalService.update(id,personalUpdateteDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Personal> deletePersonal(@PathVariable UUID id){
        log.info("Borrando persosonal por id: "+ id );
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

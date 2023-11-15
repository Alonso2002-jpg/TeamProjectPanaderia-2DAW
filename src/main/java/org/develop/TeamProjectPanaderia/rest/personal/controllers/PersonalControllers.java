package org.develop.TeamProjectPanaderia.rest.personal.controllers;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.rest.personal.models.Personal;
import org.develop.TeamProjectPanaderia.rest.personal.services.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/personal")
public class PersonalControllers {
    private final PersonalService personalService;


    @Autowired
    public PersonalControllers(PersonalService personalService) {
        this.personalService = personalService;

    }
    @GetMapping
    public ResponseEntity<List<Personal>> getAllPersonal(
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String categoria){
        log.info("Buscando todo todo el perosnal con categoría");
        return ResponseEntity.ok(personalService.findAll(isActive, categoria));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Personal> getPresonalfindById(@PathVariable String id){
        log.info("buscando por id");
        return ResponseEntity.ok(personalService.findById(id));

    }
    @PostMapping
    public ResponseEntity<Personal> createPersonal(@PathVariable PersonalCreateDto productoCreatedto){
        log.info("create personal: "+productoCreatedto+" productoCreatedto");
        return ResponseEntity.status(HttpStatus.CREATED).body(personalService.save(productoCreatedto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Personal> updatePersonal(@PathVariable String id, @Valid @RequestBody PersonalUpdateDto personalUpdateteDto){
        log.info("update personal: "+ id+" producto: "+personalUpdateteDto);
        return ResponseEntity.ok(personalService.update(id,personalUpdateteDto));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Personal> updatePartialPersonal(@PathVariable String id,@Valid @RequestBody PersonalUpdateDto personalUpdateteDto){
        log.info("actualizando partialmente personal: "+ id+" product");
        return ResponseEntity.ok(personalService.update(id,personalUpdateteDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Personal> deletePersonal(@PathVariable String id){
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

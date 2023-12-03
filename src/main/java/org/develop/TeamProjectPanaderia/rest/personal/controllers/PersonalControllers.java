package org.develop.TeamProjectPanaderia.rest.personal.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
@RestController
@Slf4j
@RequestMapping("${api.version}/personal")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Personal", description = "Endpoint de Personal de nuestra tienda")
public class PersonalControllers {
    private final PersonalService personalService;
    private final PersonalMapper personalMapper;

    @Autowired
    public PersonalControllers(PersonalService personalService, PersonalMapper personalMapper) {
        this.personalService = personalService;
        this.personalMapper = personalMapper;
    }

    @Operation(summary = "Obtiene todo el personal", description = "Obtiene una lista del personal")
    @Parameters({
            @Parameter(name = "nombre", description = "Nombre del trabajador", example = "Joselyn Obando"),
            @Parameter(name = "dni", description = "DNI del trabajador", example = "03480731B"),
            @Parameter(name = "categoria", description = "Categoria del trabajador", example = "Panadero"),
            @Parameter(name = "isActivo", description = "Si está activo o no el trabajador", example = "true"),
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo de ordenación", example = "id"),
            @Parameter(name = "direction", description = "Dirección de ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de personal"),
    })
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

    @Operation(summary = "Obtiene un trabajador por su id", description = "Obtiene un trabajador por su id")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del trabajador", example = "1a70f426-d51f-4a13-ba39-89203f94ed74", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trabajador"),
            @ApiResponse(responseCode = "400", description = "UUID invalido o de formato incorrecto"),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<PersonalResponseDto> getPersonalById(@PathVariable String id){
        log.info("Se optiene el Personal por id: " + id);
        return ResponseEntity.ok(personalMapper.toResponseDto(personalService.findById(id)));
    }

    @Operation(summary = "Crea un trabajador", description = "Crea un trabajador")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Trabajador a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trabajador creado"),
            @ApiResponse(responseCode = "400", description = "Trabajador no válido"),
    })
    @PostMapping
    public ResponseEntity<PersonalResponseDto>createPersonal(@RequestBody @Valid PersonalCreateDto personalCreateDto){
        log.info("Se crea el Personal: " + personalCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(personalMapper.toResponseDto(personalService.save(personalCreateDto)));
    }

    @Operation(summary = "Actualiza un trabajador", description = "Actualiza un trabajador")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del trabajador", example = "1a70f426-d51f-4a13-ba39-89203f94ed74", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Trabajador a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trabajador actualizado"),
            @ApiResponse(responseCode = "400", description = "Trabajador no válido"),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<PersonalResponseDto> updatePersonal(@PathVariable String id, @RequestBody @Valid PersonalUpdateDto personalUpdateDto){
        log.info("Se actualiza el Personal: " + personalUpdateDto);
        return ResponseEntity.ok(personalMapper.toResponseDto(personalService.update(id,personalUpdateDto)));
    }

    @Operation(summary = "Actualiza parcialmente un trabajador", description = "Actualiza parcialmente un trabajador")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del trabajador", example = "1a70f426-d51f-4a13-ba39-89203f94ed74", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Trabajador a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trabajador actualizado"),
            @ApiResponse(responseCode = "400", description = "Trabajador no válido"),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado"),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<PersonalResponseDto> patchPersonal(@PathVariable String id, @RequestBody @Valid PersonalUpdateDto personalUpdateDto){
        log.info("Se actualiza el Personal: " + personalUpdateDto);
        return ResponseEntity.ok(personalMapper.toResponseDto(personalService.update(id,personalUpdateDto)));
    }

    @Operation(summary = "Borra un trabajador", description = "Borra un trabajador")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del trabajador", example = "1a70f426-d51f-4a13-ba39-89203f94ed74", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trabajador borrado"),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado"),
    })
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

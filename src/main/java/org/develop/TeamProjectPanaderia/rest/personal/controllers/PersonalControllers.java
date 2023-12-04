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


/**
 * Clase controladora para manejar puntos finales REST relacionados con el personal de una tienda.
 * Proporciona operaciones como recuperar una lista de personal y obtener un trabajador por su identificador único.
 *
 * @RestController Indica que esta clase define un conjunto de puntos finales RESTful.
 * @Slf4j Anotación de Lombok para la generación automática del campo de registro.
 * @RequestMapping("${api.version}/personal") Especifica la ruta base URL para todos los puntos finales de esta clase, utilizando una propiedad para la versión de la API.
 * @PreAuthorize("hasRole('ADMIN')") Asegura que solo los usuarios con el rol 'ADMIN' puedan acceder a los puntos finales de esta clase.
 * @Tag(name = "Personal", description = "Endpoint de Personal de nuestra tienda") Proporciona metadatos para la documentación de la API, etiquetando estos puntos finales como relacionados con el personal.
 */
@RestController
@Slf4j
@RequestMapping("${api.version}/personal")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Personal", description = "Endpoint de Personal de nuestra tienda")
public class PersonalControllers {
    private final PersonalService personalService;
    private final PersonalMapper personalMapper;


    /**
     * Constructor para la clase {@code PersonalControllers}.
     *
     * @param personalService Servicio para manejar la lógica de negocio relacionada con el personal.
     * @param personalMapper Mapper para convertir entre entidades y objetos DTO para el personal.
     */
    @Autowired
    public PersonalControllers(PersonalService personalService, PersonalMapper personalMapper) {
        this.personalService = personalService;
        this.personalMapper = personalMapper;
    }


    /**
     * Recupera una página de personal basada en parámetros de consulta opcionales.
     *
     * @param nombre    Parámetro opcional para filtrar por nombre del trabajador.
     * @param dni       Parámetro opcional para filtrar por DNI del trabajador.
     * @param categoria Parámetro opcional para filtrar por categoría del trabajador.
     * @param isActivo  Parámetro opcional para filtrar por estado activo del trabajador.
     * @param page      Número de página para la paginación (el valor predeterminado es 0).
     * @param size      Tamaño de la página para la paginación (el valor predeterminado es 10).
     * @param sortBy    Campo para ordenar los resultados (el valor predeterminado es "id").
     * @param direction Dirección de ordenación (el valor predeterminado es "asc").
     * @return ResponseEntity con una página de personal como PersonalResponseDto envuelto en un PageResponse.
     * @Operation(summary = "Obtiene todo el personal", description = "Obtiene una lista del personal")
     * @Parameters({
     *   @Parameter(name = "nombre", description = "Nombre del trabajador", example = "Joselyn Obando"),
     *   @Parameter(name = "dni", description = "DNI del trabajador", example = "03480731B"),
     *   @Parameter(name = "categoria", description = "Categoria del trabajador", example = "Panadero"),
     *   @Parameter(name = "isActivo", description = "Si está activo o no el trabajador", example = "true"),
     *   @Parameter(name = "page", description = "Número de página", example = "0"),
     *   @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
     *   @Parameter(name = "sortBy", description = "Campo de ordenación", example = "id"),
     *   @Parameter(name = "direction", description = "Dirección de ordenación", example = "asc")
     * })
     * @ApiResponses(value = {
     *   @ApiResponse(responseCode = "200", description = "Página de personal"),
     * })
     * @GetMapping
     */
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

    /**
     * Recupera un trabajador por su identificador único.
     *
     * @param id Identificador único del trabajador.
     * @return ResponseEntity con los detalles del trabajador como PersonalResponseDto.
     * @Operation(summary = "Obtiene un trabajador por su id", description = "Obtiene un trabajador por su id")
     * @Parameters({
     *   @Parameter(name = "id", description = "Identificador unico del trabajador", example = "1a70f426-d51f-4a13-ba39-89203f94ed74", required = true)
     * })
     * @ApiResponses(value = {
     *   @ApiResponse(responseCode = "200", description = "Trabajador"),
     *   @ApiResponse(responseCode = "400", description = "UUID invalido o de formato incorrecto"),
     *   @ApiResponse(responseCode = "404", description = "Trabajador no encontrado"),
     * })
     * @GetMapping("/{id}")
     */
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

    /**
     * Crea un nuevo trabajador con la información proporcionada.
     *
     * @param personalCreateDto DTO con la información del nuevo trabajador.
     * @return ResponseEntity con el DTO del trabajador recién creado y el código de estado 201 (CREATED).
     * @Operation(summary = "Crea un trabajador", description = "Crea un trabajador")
     * @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Trabajador a crear", required = true)
     * @ApiResponses(value = {
     *     @ApiResponse(responseCode = "201", description = "Trabajador creado"),
     *     @ApiResponse(responseCode = "400", description = "Trabajador no válido"),
     * })
     * @PostMapping
     */
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

    /**
     * Actualiza la información de un trabajador existente con el ID proporcionado.
     *
     * @param id                Identificador único del trabajador a actualizar.
     * @param personalUpdateDto DTO con la nueva información del trabajador.
     * @return ResponseEntity con el DTO del trabajador actualizado y el código de estado 200 (OK).
     * @Operation(summary = "Actualiza un trabajador", description = "Actualiza un trabajador")
     * @Parameters({
     *     @Parameter(name = "id", description = "Identificador unico del trabajador", example = "1a70f426-d51f-4a13-ba39-89203f94ed74", required = true)
     * })
     * @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Trabajador a actualizar", required = true)
     * @ApiResponses(value = {
     *     @ApiResponse(responseCode = "200", description = "Trabajador actualizado"),
     *     @ApiResponse(responseCode = "400", description = "Trabajador no válido"),
     *     @ApiResponse(responseCode = "404", description = "Trabajador no encontrado"),
     * })
     * @PutMapping("/{id}")
     */
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



    /**
     * Actualiza parcialmente la información de un trabajador existente con el ID proporcionado.
     *
     * @param id                Identificador único del trabajador a actualizar parcialmente.
     * @param personalUpdateDto DTO con la nueva información del trabajador.
     * @return ResponseEntity con el DTO del trabajador actualizado y el código de estado 200 (OK).
     * @Operation(summary = "Actualiza parcialmente un trabajador", description = "Actualiza parcialmente un trabajador")
     * @Parameters({
     *     @Parameter(name = "id", description = "Identificador unico del trabajador", example = "1a70f426-d51f-4a13-ba39-89203f94ed74", required = true)
     * })
     * @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Trabajador a actualizar", required = true)
     * @ApiResponses(value = {
     *     @ApiResponse(responseCode = "200", description = "Trabajador actualizado"),
     *     @ApiResponse(responseCode = "400", description = "Trabajador no válido"),
     *     @ApiResponse(responseCode = "404", description = "Trabajador no encontrado"),
     * })
     * @PatchMapping("/{id}")
     */
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

    /**
     * Elimina un trabajador existente con el ID proporcionado.
     *
     * @param id Identificador único del trabajador a eliminar.
     * @return ResponseEntity con el código de estado 204 (NO CONTENT) indicando que el trabajador ha sido eliminado con éxito.
     * @Operation(summary = "Borra un trabajador", description = "Borra un trabajador")
     * @Parameters({
     *     @Parameter(name = "id", description = "Identificador unico del trabajador", example = "1a70f426-d51f-4a13-ba39-89203f94ed74", required = true)
     * })
     * @ApiResponses(value = {
     *     @ApiResponse(responseCode = "204", description = "Trabajador borrado"),
     *     @ApiResponse(responseCode = "404", description = "Trabajador no encontrado"),
     * })
     * @DeleteMapping("/{id}")
     */
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

    /**
     * Maneja las excepciones de validación lanzadas por el framework Spring cuando falla la validación de argumentos del método.
     *
     * @param ex Excepción de validación que contiene detalles sobre los errores de validación.
     * @return Mapa que contiene los nombres de los campos y sus respectivos mensajes de error de validación.
     * @ResponseStatus(HttpStatus.BAD_REQUEST) Indica que el código de estado de la respuesta HTTP será 400 (Bad Request).
     * @ExceptionHandler(MethodArgumentNotValidException.class) Indica que este método manejará excepciones del tipo MethodArgumentNotValidException.
     */
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

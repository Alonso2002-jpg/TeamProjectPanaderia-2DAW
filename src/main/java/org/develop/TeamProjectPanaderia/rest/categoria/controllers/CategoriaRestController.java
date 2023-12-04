package org.develop.TeamProjectPanaderia.rest.categoria.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.rest.categoria.mapper.CategoriaMapper;
import org.develop.TeamProjectPanaderia.rest.categoria.services.CategoriaService;
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
 * Controlador REST que gestiona las operaciones relacionadas con las categorias en la tienda.
 * Requiere el rol de usuario para acceder a las operaciones.
 */
@RestController
@RequestMapping("${api.version}/categoria")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Categorias", description = "Endpoint de Categorias de nuestra tienda")
public class CategoriaRestController {
    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    /**
     * Constructor del controlador CategoriaRestController.
     *
     * @param categoriaService Servicio de categoria a inyectar.
     * @param categoriaMapper Mapper de categoria a inyectar.
     */
    @Autowired
    public CategoriaRestController(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

    /**
     * Obtiene todas las categorias paginadas y opcionalmente filtradas.
     *
     * @param isActive   Indica si la categoria esta activa o no.
     * @param name       Nombre de la categoria.
     * @param page       Número de pagina.
     * @param size       Tamaño de la pagina.
     * @param sortBy     Campo de ordenacion.
     * @param direction  Dirección de ordenacion.
     * @return Respuesta HTTP que contiene la pagina de categorias.
     */
    @Operation(summary = "Obtiene todas las categorias", description = "Obtiene una lista de categorias")
    @Parameters({
            @Parameter(name = "isActive", description = "Si esta activo o no la categoria", example = "true"),
            @Parameter(name = "name", description = "Nombre de la categoria", example = "Reposteria"),
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo de ordenación", example = "id"),
            @Parameter(name = "direction", description = "Dirección de ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de categorias"),
    })
    @GetMapping
    public ResponseEntity<PageResponse<CategoriaResponseDto>> findAll(@RequestParam(required = false) Optional<Boolean> isActive,
                                                           @RequestParam(required = false) Optional<String> name,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                           @RequestParam(defaultValue = "asc") String direction){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(categoriaMapper.toPageResponse(categoriaService.findAll(isActive, name,pageable)), sortBy, direction));
    }

    @Operation(summary = "Obtiene una categoria por su id", description = "Obtiene una categoria por su id")
    @Parameters({
            @Parameter(name = "id", description = "Identificador de la categoria", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria"),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada"),
    })

    /**
     * Obtiene una categoria por su identificador.
     *
     * @param id Identificador de la categoria.
     * @return Respuesta HTTP que contiene la categoria.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> findById(@PathVariable Long id){
        var categoria = this.categoriaService.findById(id);
        return ResponseEntity.ok(categoriaMapper.toResponse(categoria));
    }

    /**
     * Crea una nueva categoria.
     *
     * @param categoria Categoria a crear.
     * @return Respuesta HTTP que contiene la categoria creada.
     */
    @Operation(summary = "Crea una categoria", description = "Crea una categoria")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Categoria a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria creada"),
            @ApiResponse(responseCode = "400", description = "Categoria no válida"),
    })

    /**
     * Actualiza una categoria existente.
     *
     * @param id       Identificador único de la categoria a actualizar.
     * @param categoria Categoria actualizada.
     * @return Respuesta HTTP que contiene la categoria actualizada.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaResponseDto> postCategoria(@Valid @RequestBody CategoriaCreateDto categoria){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaMapper.toResponse(categoriaService.save(categoria)));
    }

    @Operation(summary = "Actualiza una categoria", description = "Actualiza una categoria")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico de la categoria", example = "1", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Categoria a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria actualizada"),
            @ApiResponse(responseCode = "400", description = "Categoria no válida"),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada"),
    })
    /**
     * Actualiza una categora existente.
     *
     * @param id       Identificador unico de la categoria a actualizar.
     * @param categoria Categoria actualizada.
     * @return Respuesta HTTP que contiene la categoria actualizada.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaResponseDto> putCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaUpdateDto categoria){
        return ResponseEntity.ok(categoriaMapper.toResponse(categoriaService.update(id,categoria)));
    }

    @Operation(summary = "Borra una categoria", description = "Borra una categoria")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico de la categoria", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria borrada"),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada"),
    })

    /**
     * Borra una categoria por su identificador.
     *
     * @param id Identificador único de la categoria a borrar.
     * @return Respuesta HTTP que indica el exito de la operacion.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Maneja las excepciones de validacion en las operaciones del controlador.
     *
     * @param ex Excepcion de validacion.
     * @return Mapa de errores de validacion.
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

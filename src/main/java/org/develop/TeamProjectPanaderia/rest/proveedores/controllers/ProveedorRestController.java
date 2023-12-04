package org.develop.TeamProjectPanaderia.rest.proveedores.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorResponseDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.mapper.ProveedorMapper;
import org.develop.TeamProjectPanaderia.rest.proveedores.services.ProveedorService;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para manejar operaciones relacionadas con proveedores.
 */
@RestController
@RequestMapping("${api.version}/proveedores")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Proveedores", description = "Endpoint de Proveedores de nuestra panaderia")
public class ProveedorRestController {
    private final ProveedorMapper proveedorMapper;
    private final ProveedorService proveedorService;

    /**
     * Constructor que inyecta dependencias necesarias para el controlador de proveedores.
     *
     * @param proveedorMapper Mapper para convertir entre DTO y entidad para proveedores.
     * @param proveedorService Servicio para realizar operaciones relacionadas con proveedores.
     */
    @Autowired
    public ProveedorRestController(ProveedorMapper proveedorMapper,
                                   ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
        this.proveedorMapper = proveedorMapper;
    }

    /**
     * Obtiene todos los proveedores paginados y filtrados según los parámetros proporcionados.
     *
     * @param nif Nif del proveedor a filtrar.
     * @param name Nombre del proveedor a filtrar.
     * @param isActive Estado de activación del proveedor a filtrar.
     * @param tipo Tipo de proveedor a filtrar.
     * @param page Número de página.
     * @param size Tamaño de la página.
     * @param sortBy Campo de ordenación.
     * @param direction Dirección de ordenación.
     * @return Respuesta con la página de proveedores.
     */
    @Operation(summary = "Obtiene todos los proveedores", description = "Obtiene una lista de proveedores")
    @Parameters({
            @Parameter(name = "nif", description = "Nif del proveedor", example = "12345678Z"),
            @Parameter(name = "name", description = "Nombre del proveedor", example = "Harinas S.L."),
            @Parameter(name = "isActive", description = "Si esta activo o no el proveedor", example = "true"),
            @Parameter(name = "tipo", description = "Tipo de proveedor", example = "Distribuidor"),
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo de ordenación", example = "id"),
            @Parameter(name = "direction", description = "Dirección de ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de proveedores"),
    })
    @GetMapping
    public ResponseEntity<PageResponse<ProveedorResponseDto>> findAll(@RequestParam(required = false) Optional<String> nif,
                                                                      @RequestParam(required = false) Optional<String> name,
                                                                      @RequestParam(required = false) Optional<Boolean> isActive,
                                                                      @RequestParam(required = false) Optional<String> tipo,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                                      @RequestParam(defaultValue = "asc") String direction){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(proveedorMapper.toPageResponse(proveedorService.findAll(nif, name,isActive,tipo,pageable)), sortBy, direction));
    }

    /**
     * Obtiene un proveedor por su identificador único.
     *
     * @param id Identificador único del proveedor.
     * @return Respuesta con el proveedor encontrado.
     */
    @Operation(summary = "Obtiene un proveedor por su id", description = "Obtiene un proveedor por su id")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del proveedor", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDto> getProveedorById(@PathVariable Long id) {
        var proveedor = proveedorService.getProveedoresById(id);
        return ResponseEntity.ok(proveedorMapper.toResponse(proveedor));
    }

    /**
     * Crea un nuevo proveedor.
     *
     * @param proveedor DTO con la información del proveedor a crear.
     * @return Respuesta con el proveedor creado.
     */
    @Operation(summary = "Crea un proveedor", description = "Crea un proveedor")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Proveedor a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proveedor creado"),
            @ApiResponse(responseCode = "400", description = "Proveedor no válido"),
    })
    @PostMapping
    public ResponseEntity<ProveedorResponseDto> createProveedor(@Valid @RequestBody ProveedorCreateDto proveedor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorMapper.toResponse(proveedorService.saveProveedores(proveedor)));
    }

    /**
     * Actualiza la información de un proveedor existente.
     *
     * @param id Identificador único del proveedor a actualizar.
     * @param proveedor DTO con la información actualizada del proveedor.
     * @return Respuesta con el proveedor actualizado.
     */
    @Operation(summary = "Actualiza un proveedor", description = "Actualiza un proveedor")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del proveedor", example = "1", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Proveedor a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado"),
            @ApiResponse(responseCode = "400", description = "Proveedor no válido"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponseDto> updateProveedor(@PathVariable Long id, @Valid @RequestBody ProveedorUpdateDto proveedor) {
        return ResponseEntity.ok(proveedorMapper.toResponse(proveedorService.updateProveedor(proveedor,id)));
    }

    /**
     * Borra un proveedor por su identificador único.
     *
     * @param id Identificador único del proveedor a borrar.
     * @return Respuesta con el resultado de la operación.
     */
    @Operation(summary = "Borra un proveedor", description = "Borra un proveedor")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del proveedor", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proveedor borrado"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProveedor(@PathVariable Long id) {
        proveedorService.deleteProveedoresById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Maneja las excepciones de validación y devuelve un mapa de errores.
     *
     * @param ex Excepción de validación.
     * @return Mapa de errores con los campos y mensajes asociados.
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

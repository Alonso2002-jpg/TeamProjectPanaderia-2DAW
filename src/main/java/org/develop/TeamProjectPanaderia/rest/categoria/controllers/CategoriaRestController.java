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

@RestController
@RequestMapping("${api.version}/categoria")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Categorias", description = "Endpoint de Categorias de nuestra tienda")
public class CategoriaRestController {
    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    @Autowired
    public CategoriaRestController(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

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
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> findById(@PathVariable Long id){
        var categoria = this.categoriaService.findById(id);
        return ResponseEntity.ok(categoriaMapper.toResponse(categoria));
    }

    @Operation(summary = "Crea una categoria", description = "Crea una categoria")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Categoria a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria creada"),
            @ApiResponse(responseCode = "400", description = "Categoria no válida"),
    })
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
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
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

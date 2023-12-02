package org.develop.TeamProjectPanaderia.rest.proveedores.controllers;

import jakarta.validation.Valid;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorResponseDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.mapper.ProveedorMapper;
import org.develop.TeamProjectPanaderia.rest.proveedores.services.ProveedorService;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
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

@RestController
@RequestMapping("${api.version}/proveedores")
@PreAuthorize("hasRole('ADMIN')")
public class ProveedorRestController {
    private final ProveedorMapper proveedorMapper;
    private final ProveedorService proveedorService;

    public ProveedorRestController(ProveedorMapper proveedorMapper,
                                   ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
        this.proveedorMapper = proveedorMapper;
    }

    // Endpoint para obtener todos los proveedores
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

    // Endpoint para obtener un proveedor por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDto> getProveedorById(@PathVariable Long id) {
        var proveedor = proveedorService.getProveedoresById(id);
        return ResponseEntity.ok(proveedorMapper.toResponse(proveedor));
    }

    // Endpoint para crear un nuevo proveedor
    @PostMapping
    public ResponseEntity<ProveedorResponseDto> createProveedor(@Valid @RequestBody ProveedorCreateDto proveedor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorMapper.toResponse(proveedorService.saveProveedores(proveedor)));
    }

    // Endpoint para actualizar un proveedor existente
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponseDto> updateProveedor(@PathVariable Long id, @Valid @RequestBody ProveedorUpdateDto proveedor) {
        return ResponseEntity.ok(proveedorMapper.toResponse(proveedorService.updateProveedor(proveedor,id)));
    }

    // Endpoint para eliminar un proveedor por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProveedor(@PathVariable Long id) {
        proveedorService.deleteProveedoresById(id);
        return ResponseEntity.noContent().build();
    }

    //Para mostrar los errores.
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

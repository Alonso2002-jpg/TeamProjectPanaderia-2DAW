package org.develop.TeamProjectPanaderia.proveedores.controllers;

import org.develop.TeamProjectPanaderia.proveedores.exceptions.ProveedorNotFoundException;
import org.develop.TeamProjectPanaderia.proveedores.exceptions.ProveedorNotSaveException;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.proveedores.repositories.ProveedorRepository;
import org.develop.TeamProjectPanaderia.proveedores.services.ProveedorService;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/proveedores")
public class ProveedorRestController {
    private final ProveedorRepository proveedoresRepository;
    private final ProveedorService proveedorService;

    public ProveedorRestController(ProveedorRepository proveedoresRepository, ProveedorService proveedorService) {
        this.proveedoresRepository = proveedoresRepository;
        this.proveedorService = proveedorService;
    }

    // Endpoint para obtener todos los proveedores
    @GetMapping
    public ResponseEntity<PageResponse<Proveedor>> findAll(@RequestParam(required = false) Optional<String> nif,
                                                           @RequestParam(required = false) Optional<String> name,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                           @RequestParam(defaultValue = "asc") String direction){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(proveedorService.findAll(nif, name,pageable), sortBy, direction));
    }

    // Endpoint para obtener un proveedor por su ID
    @GetMapping("/{id}")
    public Proveedor getProveedorById(@PathVariable Long id) {
        return proveedoresRepository.findById(id)
                .orElseThrow(() -> new ProveedorNotFoundException(id));
    }

    // Endpoint para crear un nuevo proveedor
    @PostMapping
    public Proveedor createProveedor(@RequestBody Proveedor proveedor) {
        Proveedor ProveedorGuardado = proveedoresRepository.save(proveedor);
        if (ProveedorGuardado == null) {
            throw new ProveedorNotSaveException("No se pudo guardar el proveedor");
        }
        return ProveedorGuardado;
    }

    // Endpoint para actualizar un proveedor existente
    @PutMapping("/{id}")
    public Proveedor updateProveedor(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        proveedor.setId(id);
        return proveedoresRepository.save(proveedor);
    }

    // Endpoint para eliminar un proveedor por su ID
    @DeleteMapping("/{id}")
    public void deleteProveedor(@PathVariable Long id) {
        proveedoresRepository.deleteById(id);
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


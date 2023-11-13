package org.develop.TeamProjectPanaderia.proveedores.controllers;

import org.develop.TeamProjectPanaderia.proveedores.exceptions.ProveedorNotFoundException;
import org.develop.TeamProjectPanaderia.proveedores.exceptions.ProveedorNotSaveException;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.proveedores.repositories.ProveedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/proveedores")
public class ProveedorRestController {
    private final ProveedorRepository proveedoresRepository;

    public ProveedorRestController(ProveedorRepository proveedoresRepository) {
        this.proveedoresRepository = proveedoresRepository;
    }

    // Endpoint para obtener todos los proveedores
    @GetMapping
    public List<Proveedor> getAllProveedores() {
        return proveedoresRepository.findAll();
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


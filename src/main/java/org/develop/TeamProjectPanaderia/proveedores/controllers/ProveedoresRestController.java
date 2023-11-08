package org.develop.TeamProjectPanaderia.proveedores.controllers;

import org.develop.TeamProjectPanaderia.proveedores.exceptions.ProveedoresNotFoundException;
import org.develop.TeamProjectPanaderia.proveedores.exceptions.ProveedoresNotSaveException;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedores;
import org.develop.TeamProjectPanaderia.proveedores.repositories.ProveedoresRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/proveedores")
public class ProveedoresRestController {
    private final ProveedoresRepository proveedoresRepository;

    public ProveedoresRestController(ProveedoresRepository proveedoresRepository) {
        this.proveedoresRepository = proveedoresRepository;
    }

    // Endpoint para obtener todos los proveedores
    @GetMapping
    public Iterable<Proveedores> getAllProveedores() {
        return proveedoresRepository.findAll();
    }

    // Endpoint para obtener un proveedor por su ID
    @GetMapping("/{id}")
    public Proveedores getProveedorById(@PathVariable Long id) {
        return proveedoresRepository.findById(id)
                .orElseThrow(() -> new ProveedoresNotFoundException(id));
    }

    // Endpoint para crear un nuevo proveedor
    @PostMapping
    public Proveedores createProveedor(@RequestBody Proveedores proveedor) {
        Proveedores ProveedorGuardado = proveedoresRepository.save(proveedor);
        if (ProveedorGuardado == null) {
            throw new ProveedoresNotSaveException("No se pudo guardar el proveedor");
        }
        return ProveedorGuardado;
    }

    // Endpoint para actualizar un proveedor existente
    @PutMapping("/{id}")
    public Proveedores updateProveedor(@PathVariable Long id, @RequestBody Proveedores proveedor) {
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


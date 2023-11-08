package org.develop.TeamProjectPanaderia.producto.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.develop.TeamProjectPanaderia.producto.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/producto")
public class ProductoRestController {
    private final ProductoService productoService;

    @Autowired
    public ProductoRestController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String proveedor) {
    log.info("Buscando todos los productos con categoria: " + categoria + " y proveedor: " + proveedor);
    return ResponseEntity.ok(productoService.findAll(categoria, proveedor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable UUID id){
        log.info("Buscando producto por id: " + id);
        return ResponseEntity.ok(productoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Producto> createProduct(@Valid @RequestBody ProductoCreateDto productoCreateDto){
        log.info("Creando producto: " + productoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.save(productoCreateDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductoUpdateDto productoUpdateDto){
        log.info("Actualizando producto por id: " + id + " con producto: " + productoUpdateDto);
        return ResponseEntity.ok(productoService.update(id, productoUpdateDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Producto> updatePartialProduct(@PathVariable UUID id, @Valid @RequestBody ProductoUpdateDto productoUpdateDto){
        log.info("Actualizando producto por id: " + id + " con producto: " + productoUpdateDto);
        return ResponseEntity.ok(productoService.update(id, productoUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id){
        log.info("Borrando producto por id: " + id);
        productoService.deleteById(id);
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

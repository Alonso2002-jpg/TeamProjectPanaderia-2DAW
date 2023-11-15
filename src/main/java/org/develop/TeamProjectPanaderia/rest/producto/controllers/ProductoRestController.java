package org.develop.TeamProjectPanaderia.rest.producto.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoResponseDto;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.rest.producto.mapper.ProductoMapper;
import org.develop.TeamProjectPanaderia.rest.producto.services.ProductoService;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/producto")
public class ProductoRestController {
    private final ProductoService productoService;
    private final ProductoMapper productoMapper;
    @Autowired
    public ProductoRestController(ProductoService productoService, ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductoResponseDto>> getAllProductos(
            @RequestParam(required = false) Optional <String> nombre,
            @RequestParam(required = false) Optional <Integer> stockMin,
            @RequestParam(required = false) Optional <Double> precioMax,
            @RequestParam(required = false) Optional <Boolean> isActivo,
            @RequestParam(required = false) Optional <String> categoria,
            @RequestParam(required = false) Optional <String> proveedor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        log.info("Buscando todos los productos con las siguientes opciones: " + nombre + " " + stockMin + " " + precioMax + " " + isActivo + " " + categoria + " " + proveedor );
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(productoMapper.toPageResponse(productoService.findAll(nombre, stockMin, precioMax, isActivo, categoria, proveedor, pageable)), sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> getProductoById(@PathVariable String id){
        log.info("Buscando producto por id: " + id);
        return ResponseEntity.ok(productoMapper.toProductoResponseDto(productoService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDto> createProduct(@Valid @RequestBody ProductoCreateDto productoCreateDto){
        log.info("Creando producto: " + productoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoMapper.toProductoResponseDto(productoService.save(productoCreateDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> updateProduct(@PathVariable String id, @Valid @RequestBody ProductoUpdateDto productoUpdateDto){
        log.info("Actualizando producto por id: " + id + " con producto: " + productoUpdateDto);
        return ResponseEntity.ok(productoMapper.toProductoResponseDto(productoService.update(id, productoUpdateDto)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> updatePartialProduct(@PathVariable String id, @Valid @RequestBody ProductoUpdateDto productoUpdateDto){
        log.info("Actualizando parcialmente producto con id: " + id + " con producto: " + productoUpdateDto);
        return ResponseEntity.ok(productoMapper.toProductoResponseDto(productoService.update(id, productoUpdateDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id){
        log.info("Borrando producto por id: " + id);
        productoService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoResponseDto> updateImage(@PathVariable String id, @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()){
            return ResponseEntity.ok(productoMapper.toProductoResponseDto(productoService.updateImg(id,file)));
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La Imagen no puede estar vacia");
        }
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

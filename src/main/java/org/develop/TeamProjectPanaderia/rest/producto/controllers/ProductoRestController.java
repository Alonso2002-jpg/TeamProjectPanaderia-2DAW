package org.develop.TeamProjectPanaderia.rest.producto.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("${api.version}/producto")
@Tag(name = "Productos", description = "Endpoint de Productos de nuestra panaderia")
public class ProductoRestController {
    private final ProductoService productoService;
    private final ProductoMapper productoMapper;
    @Autowired
    public ProductoRestController(ProductoService productoService, ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    @Operation(summary = "Obtiene todos los productos", description = "Obtiene una lista de productos")
    @Parameters({
            @Parameter(name = "nombre", description = "Nombre del producto", example = "Pan dulce"),
            @Parameter(name = "stockMin", description = "Cantidad minima del producto", example = "10"),
            @Parameter(name = "precioMax", description = "Precio maximo del producto", example = "35.0"),
            @Parameter(name = "isActivo", description = "Si está activo o no el producto", example = "true"),
            @Parameter(name = "categoria", description = "Nombre de categoria del producto", example = "Bebidas"),
            @Parameter(name = "proveedor", description = "NIF del proveedor del producto", example = "12345678Z"),
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo de ordenación", example = "id"),
            @Parameter(name = "direction", description = "Dirección de ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de productos"),
    })
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

    @Operation(summary = "Obtiene un producto por su id", description = "Obtiene un producto por su id")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del producto", example = "1a70f426-d51f-4a13-ba39-89203f94ed74", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "UUID no valido o de forma incorrecto")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> getProductoById(@PathVariable String id){
        log.info("Buscando producto por id: " + id);
        return ResponseEntity.ok(productoMapper.toProductoResponseDto(productoService.findById(id)));
    }

    @Operation(summary = "Crea un producto", description = "Crea un producto")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Producto a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado"),
            @ApiResponse(responseCode = "400", description = "Producto no válido")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductoResponseDto> createProduct(@Valid @RequestBody ProductoCreateDto productoCreateDto){
        log.info("Creando producto: " + productoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoMapper.toProductoResponseDto(productoService.save(productoCreateDto)));
    }

    @Operation(summary = "Actualiza un producto", description = "Actualiza un producto")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del producto", example = "1a70f426-d51f-4a13-ba39-89203f94ed74", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Producto a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "400", description = "Producto no válido"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponseDto> updateProduct(@PathVariable String id, @Valid @RequestBody ProductoUpdateDto productoUpdateDto){
        log.info("Actualizando producto por id: " + id + " con producto: " + productoUpdateDto);
        return ResponseEntity.ok(productoMapper.toProductoResponseDto(productoService.update(id, productoUpdateDto)));
    }

    @Operation(summary = "Actualiza parcialmente un producto", description = "Actualiza parcialmente un producto")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del producto", example = "1a70f426-d51f-4a13-ba39-89203f94ed74", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Producto a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "400", description = "Producto no válido"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponseDto> updatePartialProduct(@PathVariable String id, @Valid @RequestBody ProductoUpdateDto productoUpdateDto){
        log.info("Actualizando parcialmente producto con id: " + id + " con producto: " + productoUpdateDto);
        return ResponseEntity.ok(productoMapper.toProductoResponseDto(productoService.update(id, productoUpdateDto)));
    }

    @Operation(summary = "Borra un producto", description = "Borra un producto")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del producto", example = "1a70f426-d51f-4a13-ba39-89203f94ed74", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto borrado"),
            @ApiResponse(responseCode = "400", description = "UUID no valido o de forma incorrecto"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id){
        log.info("Borrando producto por id: " + id);
        productoService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Actualiza la imagen de un producto", description = "Actualiza la imagen de un producto")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del producto", example = "1a70f426-d51f-4a13-ba39-89203f94ed74", required = true),
            @Parameter(name = "file", description = "Fichero a subir", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "400", description = "Producto no válido"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    })
    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponseDto> updateImage(@PathVariable String id, @RequestParam("file") MultipartFile file){
        log.info(file.toString());
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

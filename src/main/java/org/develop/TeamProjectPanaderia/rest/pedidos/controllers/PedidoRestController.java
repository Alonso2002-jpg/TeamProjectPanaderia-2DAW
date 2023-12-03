package org.develop.TeamProjectPanaderia.rest.pedidos.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.develop.TeamProjectPanaderia.rest.pedidos.model.Pedido;
import org.develop.TeamProjectPanaderia.rest.pedidos.services.PedidoService;
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

@Slf4j
@RestController
@RequestMapping("/${api.version}/pedidos")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Pedidos", description = "Endpoint de los pedidos de nuestra panaderia")
public class PedidoRestController {
    private final PedidoService pedidoService;

    @Autowired
    public PedidoRestController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Obtiene todos los pedidos", description = "Obtiene una lista de pedidos")
    @Parameters({
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo de ordenación", example = "id"),
            @Parameter(name = "direction", description = "Dirección de ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de pedidos"),
    })
    @GetMapping
    public ResponseEntity<PageResponse<Pedido>> getAllpedidos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(pedidoService.findAll(pageable), sortBy, direction));
    }

    @Operation(summary = "Obtiene un pedido por su id", description = "Obtiene un pedido por su id")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del pedido", example = "6568d6b3faf1c03ca80a6818", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedido(@PathVariable("id") ObjectId id) {
        return ResponseEntity.ok(pedidoService.findById(id));
    }


    @Operation(summary = "Obtiene los pedidos de un usuario por su id", description = "Obtiene los pedidos de un usuario por su id")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del usuario", example = "1", required = true),
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo de ordenación", example = "id"),
            @Parameter(name = "direction", description = "Dirección de ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<PageResponse<Pedido>> getPedidosByUser(@PathVariable("id")Long id,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size,
                                                                 @RequestParam(defaultValue = "id") String sortBy,
                                                                 @RequestParam(defaultValue = "asc") String direction) {
    log.info("Obteniendo Pedido de usuario con Id: " + id);
    Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    return ResponseEntity.ok(PageResponse.of(pedidoService.findByIdUsuario(id, pageable), sortBy, direction));
    }

    @Operation(summary = "Crea un pedido", description = "Crea un pedido")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pedido a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado"),
            @ApiResponse(responseCode = "400", description = "Pedido no válido"),
    })
    @PostMapping
    public ResponseEntity<Pedido> createPedido(@Valid @RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.save(pedido));
    }

    @Operation(summary = "Actualiza un pedido", description = "Actualiza un pedido")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del pedido", example = "6568d6b3faf1c03ca80a6818", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pedido a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado"),
            @ApiResponse(responseCode = "400", description = "Pedido no válido"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable("id")ObjectId id,@Valid @RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.update(id, pedido));
    }

    @Operation(summary = "Borra un pedido", description = "Borra un pedido")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del pedido", example = "6568d6b3faf1c03ca80a6818", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido borrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePedido(@PathVariable("id")ObjectId id) {
        pedidoService.deleteById(id);
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

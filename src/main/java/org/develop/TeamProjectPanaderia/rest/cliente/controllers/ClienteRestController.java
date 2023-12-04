package org.develop.TeamProjectPanaderia.rest.cliente.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteResponseDto;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.mapper.ClienteMapper;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.rest.cliente.services.ClienteService;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
/**
 * {@code ClienteRestController} es un controlador REST de Spring responsable de gestionar
 * operaciones CRUD relacionadas con clientes en nuestra tienda. Proporciona puntos de conexión
 * para obtener todos los clientes, obtener un cliente por ID, crear un nuevo cliente, actualizar
 * un cliente, actualizar un cliente parcialmente, eliminar un cliente y actualizar la imagen de un cliente.
 *
 * Este controlador está asegurado con control de acceso basado en roles, y solo los usuarios con
 * el rol 'ADMIN' tienen permitido acceder a sus puntos de conexión.
 *
 * @RestController Indica que esta clase sirve como un controlador de servicio web RESTful.
 * @Slf4j Simple Logging Facade for Java, proporciona capacidades de registro utilizando SLF4J.
 * @RequestMapping("/api.version/cliente") Mapea la URL base para todos los puntos de conexión en este controlador.
 * @PreAuthorize("hasRole('ADMIN')") Especifica que solo los usuarios con el rol 'ADMIN' tienen autorización para acceder a los puntos de conexión.
 * @Tag(name = "Clientes", description = "Endpoint de Clientes de nuestra tienda") Proporciona etiquetas de documentación para Swagger/OpenAPI.
 */
@RestController
@Slf4j
@RequestMapping("${api.version}/cliente")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Clientes", description = "Endpoint de Clientes de nuestra tienda")
public class ClienteRestController {
    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    @Autowired
    public ClienteRestController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }
    /**
     * Recupera todos los clientes en base a los parámetros proporcionados.
     *
     * @param nombreCompleto Parámetro opcional para filtrar clientes por nombre completo.
     * @param categoria Parámetro opcional para filtrar clientes por categoría.
     * @param page Número de página para paginación.
     * @param size Tamaño de página para paginación.
     * @param sortBy Campo para ordenar los resultados.
     * @param direction Dirección de ordenación ('asc' o 'desc').
     * @return ResponseEntity que contiene un PageResponse de ClienteResponseDto.
     */
    @Operation(summary = "Obtiene todos los clientes", description = "Obtiene una lista de clientes")
    @Parameters({
            @Parameter(name = "nombreCompleto", description = "Nombre completo del cliente", example = "Kurt Cobain"),
            @Parameter(name = "categoria", description = "Categoria del cliente", example = "VIP"),
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo de ordenación", example = "id"),
            @Parameter(name = "direction", description = "Dirección de ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de clientes"),
    })

    @GetMapping
    public ResponseEntity<PageResponse<ClienteResponseDto>> getAllCliente(
            @RequestParam(required = false) Optional<String> nombreCompleto,
            @RequestParam(required = false) Optional<String> categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        log.info("Buscando todos los clientes con las siguientes opciones: " + nombreCompleto + " " + categoria);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(clienteMapper.toPageClienteResponse(clienteService.findAll(nombreCompleto, categoria, pageable)), sortBy, direction));
    }

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id Identificador único del cliente.
     * @return ResponseEntity que contiene un ClienteResponseDto si el cliente es encontrado, o una respuesta de error si no se encuentra.
     */
    @Operation(summary = "Obtiene un cliente por su id", description = "Obtiene un cliente por su id")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del cliente", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> getClienteById(@PathVariable Long id) {
        log.info("Buscando cliente por id: " + id);
        return ResponseEntity.ok(clienteMapper.toClienteResponseDto(clienteService.findById(id)));
    }
    /**
     * Crea un nuevo cliente.
     *
     * @param clienteCreateDto Datos del cliente a crear.
     * @return ResponseEntity que contiene el ClienteResponseDto del cliente recién creado, o una respuesta de error si la creación falla.
     */
    @Operation(summary = "Crea un cliente", description = "Crea un cliente")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cliente a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado"),
            @ApiResponse(responseCode = "400", description = "Cliente no válido"),
    })
    @PostMapping
    public ResponseEntity<ClienteResponseDto> createCliente(@Valid @RequestBody ClienteCreateDto clienteCreateDto) {
        log.info("Creando cliente: " + clienteCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toClienteResponseDto(clienteService.save(clienteCreateDto)));
    }
    /**
     * Actualiza un cliente existente.
     *
     * @param id Identificador único del cliente a actualizar.
     * @param clienteUpdateDto Datos actualizados del cliente.
     * @return ResponseEntity que contiene el ClienteResponseDto del cliente actualizado, o una respuesta de error si la actualización falla.
     */
    @Operation(summary = "Actualiza un cliente", description = "Actualiza un cliente")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del cliente", example = "1", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cliente a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado"),
            @ApiResponse(responseCode = "400", description = "Cliente no válido"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> updateCliente(@PathVariable Long id, @Valid @RequestBody ClienteUpdateDto clienteUpdateDto) {
        log.info("Actualizando cliente por id: " + id + " con cliente: " + clienteUpdateDto);
        return ResponseEntity.ok(clienteMapper.toClienteResponseDto(clienteService.update(id, clienteUpdateDto)));
    }
    /**
     * Actualiza parcialmente un cliente existente.
     *
     * @param id Identificador único del cliente a actualizar parcialmente.
     * @param clienteUpdateDto Datos actualizados del cliente.
     * @return ResponseEntity que contiene el ClienteResponseDto del cliente actualizado parcialmente, o una respuesta de error si la actualización falla.
     */
    @Operation(summary = "Actualiza parcialmente un cliente", description = "Actualiza parcialmente un cliente")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del cliente", example = "1", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cliente a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado"),
            @ApiResponse(responseCode = "400", description = "Cliente no válido"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> updatePartialmenteCliente(@PathVariable Long id, @Valid @RequestBody ClienteUpdateDto clienteUpdateDto) {
        log.info("Actualizando parcialmente cliente con id: " + id + " con cliente: " + clienteUpdateDto);
        return ResponseEntity.ok(clienteMapper.toClienteResponseDto(clienteService.update(id, clienteUpdateDto)));
    }
    /**
     * Borra un cliente por su ID.
     *
     * @param id Identificador único del cliente a borrar.
     * @return ResponseEntity sin contenido (204) si el cliente es borrado exitosamente, o una respuesta de error (404) si el cliente no es encontrado.
     */
    @Operation(summary = "Borra un cliente", description = "Borra un cliente")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del cliente", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente borrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        log.info("Borrando cliente por id: " + id);
        clienteService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * Actualiza la imagen de un cliente.
     *
     * @param id Identificador único del cliente cuya imagen se actualizará.
     * @param file Archivo a subir como imagen del cliente.
     * @return ResponseEntity que contiene el ClienteResponseDto del cliente actualizado con la nueva imagen, o una respuesta de error si la actualización falla.
     * @throws ResponseStatusException Si el archivo de imagen está vacío, se lanza una excepción de estado de respuesta con un mensaje de error.
     */
    @Operation(summary = "Actualiza la imagen de un cliente", description = "Actualiza la imagen de un cliente")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico del cliente", example = "1", required = true),
            @Parameter(name = "file", description = "Fichero a subir", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado"),
            @ApiResponse(responseCode = "400", description = "Cliente no válido"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
    })
    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClienteResponseDto> updateImage(@PathVariable Long id, @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()){
            return ResponseEntity.ok(clienteMapper.toClienteResponseDto(clienteService.updateImg(id,file)));
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La Imagen no puede estar vacia");
        }
    }
    /**
     * Maneja excepciones de validación causadas por argumentos no válidos en los métodos del controlador.
     *
     * @param ex Excepción de validación de argumento del método.
     * @return Mapa de errores que contiene los nombres de campo y mensajes de error asociados.
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

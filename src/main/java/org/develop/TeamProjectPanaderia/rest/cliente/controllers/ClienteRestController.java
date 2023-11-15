package org.develop.TeamProjectPanaderia.rest.cliente.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteUpdateDto;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/cliente")
public class ClienteRestController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteRestController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<Cliente>> getAllCliente(
            @RequestParam(required = false) Optional<String> nombreCompleto,
            @RequestParam(required = false) Optional<String> categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        log.info("Buscando todos los clientes con las siguientes opciones: " + nombreCompleto + " " + categoria);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(clienteService.findAll(nombreCompleto, categoria, pageable), sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        log.info("Buscando cliente por id: " + id);
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Cliente> createCliente(@Valid @RequestBody ClienteCreateDto clienteCreateDto) {
        log.info("Creando cliente: " + clienteCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.save(clienteCreateDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @Valid @RequestBody ClienteUpdateDto clienteUpdateDto) {
        log.info("Actualizando cliente por id: " + id + " con cliente: " + clienteUpdateDto);
        return ResponseEntity.ok(clienteService.update(id, clienteUpdateDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cliente> updatePartialmenteCliente(@PathVariable Long id, @Valid @RequestBody ClienteUpdateDto clienteUpdateDto) {
        log.info("Actualizando parcialmente cliente con id: " + id + " con cliente: " + clienteUpdateDto);
        return ResponseEntity.ok(clienteService.update(id, clienteUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        log.info("Borrando cliente por id: " + id);
        clienteService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Cliente> updateImage(@PathVariable Long id, @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()){
            return ResponseEntity.ok(clienteService.updateImg(id,file));
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

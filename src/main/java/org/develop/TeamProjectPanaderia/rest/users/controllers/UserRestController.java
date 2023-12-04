package org.develop.TeamProjectPanaderia.rest.users.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.develop.TeamProjectPanaderia.rest.pedidos.model.Pedido;
import org.develop.TeamProjectPanaderia.rest.pedidos.services.PedidoService;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserInfoResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserRequestDto;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserResponseDto;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.develop.TeamProjectPanaderia.rest.users.services.UserService;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("${api.version}/users")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Users", description = "Endpoint para gestionar los usuarios de la Panaderia Pepitos")
public class UserRestController {
    private final UserService userService;
    private final PedidoService pedidoService;

    @Autowired
    public UserRestController(UserService userService, PedidoService pedidoService) {
        this.userService = userService;
        this.pedidoService = pedidoService;
    }

    /**
     * Obtiene todos los usuarios paginados y opcionalmente filtrados por nombre de usuario, correo electrónico o estado de activación.
     *
     * @param username  Nombre de usuario para filtrar.
     * @param email     Correo electrónico para filtrar.
     * @param isActive  Estado de activación para filtrar.
     * @param page      Número de página.
     * @param size      Tamaño de la página.
     * @param sortBy    Campo por el cual ordenar los resultados.
     * @param direction Dirección de ordenamiento (ASC o DESC).
     * @return Lista paginada de usuarios.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserResponseDto>> findAll(@RequestParam(required = false) Optional<String> username,
                                                                 @RequestParam(required = false)Optional<String> email,
                                                                 @RequestParam(required = false)Optional<Boolean> isActive,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size,
                                                                 @RequestParam(defaultValue = "id") String sortBy,
                                                                 @RequestParam(defaultValue = "ASC") String direction) {
        log.info("Obteniendo todos los usuarios");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<UserResponseDto> pageResult = userService.findAll(username, email, isActive, PageRequest.of(page, size, sort));

        return ResponseEntity.ok(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Obtiene un usuario por su identificador único.
     *
     * @param id Identificador único del usuario.
     * @return Información detallada del usuario.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserInfoResponseDto> getById(@PathVariable("id") Long id) {
        log.info("Obteniendo usuario con id: " + id);
        return ResponseEntity.ok(userService.findById(id));
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param user Datos del usuario a crear.
     * @return Datos del usuario creado.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> postUser(@RequestBody @Valid UserRequestDto user) {
        log.info("Creando usuario: " + user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    /**
     * Actualiza un usuario existente por su identificador único.
     *
     * @param id   Identificador único del usuario a actualizar.
     * @param user Datos actualizados del usuario.
     * @return Datos del usuario actualizado.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> putUser(@PathVariable("id") Long id, @RequestBody UserRequestDto user) {
        log.info("Actualizando usuario con id: " + id);
        return ResponseEntity.ok(userService.update(id, user));
    }

    /**
     * Elimina un usuario por su identificador único.
     *
     * @param id Identificador único del usuario a eliminar.
     * @return Respuesta sin contenido.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        log.info("Eliminando usuario con id: " + id);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el perfil del usuario autenticado.
     *
     * @param user Usuario autenticado.
     * @return Información detallada del usuario autenticado.
     */
    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserInfoResponseDto> meProfile(@AuthenticationPrincipal User user) {
        log.info("Obteniendo perfil de usuario");
        return ResponseEntity.ok(userService.findById(user.getId()));
    }

    /**
     * Actualiza el perfil del usuario autenticado.
     *
     * @param user            Usuario autenticado.
     * @param userRequestDto  Datos actualizados del usuario.
     * @return Datos del usuario actualizado.
     */
    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponseDto> meProfileUpdate(@AuthenticationPrincipal User user, @RequestBody UserRequestDto userRequestDto) {
        log.info("Actualizando perfil de usuario");
        return ResponseEntity.ok(userService.update(user.getId(), userRequestDto));
    }

    /**
     * Elimina el perfil del usuario autenticado.
     *
     * @param user Usuario autenticado.
     * @return Respuesta sin contenido.
     */
    @DeleteMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> meProfileDelete(@AuthenticationPrincipal User user) {
        log.info("Eliminando perfil de usuario");
        userService.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene un pedido específico del usuario actual por ID.
     *
     * @param user  El usuario autenticado.
     * @return ResponseEntity con el pedido solicitado.
     */
    @GetMapping("/me/pedidos")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<PageResponse<Pedido>> mePedidos(@AuthenticationPrincipal User user,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                          @RequestParam(defaultValue = "asc") String direction) {
        log.info("Obteniendo pedidos de usuario con Id: " + user.getId());
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(pedidoService.findByIdUsuario(user.getId(), pageable), sortBy, direction));
    }

    /**
     * Obtiene un pedido específico del usuario actual por ID.
     *
     * @param user  El usuario autenticado.
     * @param id    El ID del pedido.
     * @return ResponseEntity con el pedido solicitado.
     */
    @GetMapping("/me/pedidos/{id}")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<Pedido> mePedido(@AuthenticationPrincipal User user, @PathVariable("id") ObjectId id) {
        log.info("Obteniendo pedido con id: " + id);
        return ResponseEntity.ok(pedidoService.findById(id));
    }

    /**
     * Crea un nuevo pedido para el usuario actual.
     *
     * @param user    El usuario autenticado.
     * @param pedido  El pedido a ser creado.
     * @return ResponseEntity con el pedido creado.
     */
    @PostMapping("/me/pedidos")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<Pedido> postPedido(@AuthenticationPrincipal User user, @Valid @RequestBody Pedido pedido) {
        log.info("Creando pedido: " + pedido);
        pedido.setIdUsuario(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.save(pedido));
    }

    /**
     * Actualiza un pedido existente del usuario actual por ID.
     *
     * @param user    El usuario autenticado.
     * @param id      El ID del pedido a actualizar.
     * @param pedido  El pedido con los nuevos datos.
     * @return ResponseEntity con el pedido actualizado.
     */
    @PutMapping("/me/pedidos/{id}")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<Pedido> putPedido(@AuthenticationPrincipal User user, @PathVariable("id") ObjectId id, @Valid @RequestBody Pedido pedido) {
        log.info("Actualizando pedido con id: " + id);
        pedido.setIdUsuario(user.getId());
        return ResponseEntity.ok(pedidoService.update(id, pedido));
    }

    /**
     * Elimina un pedido del usuario actual por ID.
     *
     * @param user  El usuario autenticado.
     * @param id    El ID del pedido a eliminar.
     * @return ResponseEntity indicando el éxito de la operación.
     */
    @DeleteMapping("/me/pedidos/{id}")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<String> deletePedido(@AuthenticationPrincipal User user, @PathVariable("id") ObjectId id) {
        log.info("Eliminando pedido con id: " + id);
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Maneja las excepciones de validación lanzadas durante la creación o actualización de pedidos.
     *
     * @param ex  La excepción de validación.
     * @return Mapa que contiene los errores de validación.
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
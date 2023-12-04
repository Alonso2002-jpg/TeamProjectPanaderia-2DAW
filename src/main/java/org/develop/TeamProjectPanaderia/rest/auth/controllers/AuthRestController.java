package org.develop.TeamProjectPanaderia.rest.auth.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.rest.auth.dto.JwtAuthResponseDto;
import org.develop.TeamProjectPanaderia.rest.auth.dto.UserSignInRequest;
import org.develop.TeamProjectPanaderia.rest.auth.dto.UserSignUpRequest;
import org.develop.TeamProjectPanaderia.rest.auth.services.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Es un controlador en un entorno de aplicacion web basada en el framework Spring Boot.
 * Su funcion principal es manejar las solicitudes HTTP relacionadas con la autenticacion y autorizacion de usuarios
 * en una aplicacion.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@RestController
@Slf4j
@RequestMapping("${api.version}/auth")
@Tag(name = "Autorizacion", description = "Endpoint para la autorizacion de Logeo y registro en nuestra tienda")
public class AuthRestController {

    private final AuthenticationService authenticationService;
    /**
     * Constructor del controlador AuthRestController.
     *
     * @param authenticationService El servicio de autenticacion a inyectar.
     */
    @Autowired
    public AuthRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    /**
     * Maneja las solicitudes de registro de usuarios en la tienda.
     *
     * @param userSignUpRequest La solicitud de registro de usuario.
     * @return Una respuesta HTTP que contiene un objeto JwtAuthResponseDto.
     */
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthResponseDto> signup(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        log.info("Signup request: {}", userSignUpRequest);
        return ResponseEntity.ok(authenticationService.signUp(userSignUpRequest));
    }

    /**
     * Maneja las solicitudes de inicio de sesion de usuarios en la tienda.
     *
     * @param userSignInRequest La solicitud de inicio de sesion de usuario.
     * @return Una respuesta HTTP que contiene un objeto JwtAuthResponseDto.
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponseDto> signin(@Valid @RequestBody UserSignInRequest userSignInRequest) {
        log.info("Signin request: {}", userSignInRequest);
        return ResponseEntity.ok(authenticationService.signIn(userSignInRequest));
    }

    /**
     * Maneja las excepciones de validación generadas al validar las solicitudes.
     *
     * @param ex La excepción de validacion.
     * @return Un mapa de errores que contiene los campos y mensajes de error.
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

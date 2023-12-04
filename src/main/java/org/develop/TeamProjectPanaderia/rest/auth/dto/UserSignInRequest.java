package org.develop.TeamProjectPanaderia.rest.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un objeto de transferencia de datos (DTO) para la solicitud de inicio de sesion de usuario.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInRequest {
    @NotBlank(message = "username cannot be blank")
    private String username;
    @NotBlank(message = "password cannot be blank")
    private String password;
}


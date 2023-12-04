package org.develop.TeamProjectPanaderia.rest.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
/**
 * Clase que representa un objeto de transferencia de datos (DTO) para la solicitud de registro de usuario.
 *
 * <p>
 * Esta clase utiliza las anotaciones de Lombok para generar automaticamente constructores, metodos getter/setter,
 * y otros metodos útiles. Se utiliza para encapsular la informacion necesaria para registrar un nuevo usuario,
 * incluyendo nombre, nombre de usuario, correo electronico y contrasenas.
 * </p>
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {
    @NotBlank(message = "Nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @NotBlank(message = "Email no puede estar vacío")
    private String email;

    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 8, message = "Password debe tener al menos 5 caracteres")
    private String password;

    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 8, message = "Password de comprobación debe tener al menos 5 caracteres")
    private String passwordComprobacion;
}

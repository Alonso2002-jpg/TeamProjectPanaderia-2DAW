package org.develop.TeamProjectPanaderia.rest.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.rest.users.model.Role;

import java.util.Set;

/**
 * Clase que representa un objeto de transferencia de datos (DTO) para la respuesta de informacion de usuario.
 *
 * <p>
 * Esta clase utiliza las anotaciones de Lombok para generar automaticamente constructores, metodos getter/setter,
 * y otros metodos utiles. Se utiliza para encapsular la informacion de un usuario en las respuestas del sistema.
 * </p>
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    /**
     * Identificador único del usuario.
     */
    private Long id;
    /**
     * Nombre del usuario.
     */
    private String name;
    /**
     * Nombre de usuario unico.
     */
    private String username;
    /**
     * Correo electronico del usuario.
     */
    private String email;
    /**
     * Conjunto de roles asociados al usuario. Por defecto, se establece como ROLE.USER.
     */
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
}

package org.develop.TeamProjectPanaderia.rest.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un Data Transfer Object (DTO) para la respuesta de autenticacion con un token JWT.
 *
 *
 * Esta clase utiliza las anotaciones de Lombok para generar automaticamente constructores, metodos getter/setter
 * y otros metodos utiles. Se utiliza para encapsular la informacion de respuesta de autenticacion, que en este caso
 * consiste en un token JWT.
 *
 *
 * @see lombok.Data
 * @see lombok.Builder
 * @see lombok.NoArgsConstructor
 * @see lombok.AllArgsConstructor
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponseDto {
    String token;
}

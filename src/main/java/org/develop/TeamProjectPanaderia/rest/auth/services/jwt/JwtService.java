package org.develop.TeamProjectPanaderia.rest.auth.services.jwt;

import org.springframework.security.core.userdetails.UserDetails;
/**
 * Interfaz que define operaciones para manipular tokens JWT.
 *
 *  @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
public interface JwtService {

    /**
     * Extrae el nombre de usuario (username) contenido en un token JWT.
     *
     * @param token El token JWT del cual extraer el nombre de usuario.
     * @return El nombre de usuario extraido del token.
     */
    String extractUserName(String token);
    /**
     * Genera un token JWT para los detalles del usuario proporcionados.
     *
     * @param userDetails Detalles del usuario para los cuales generar el token JWT.
     * @return El token JWT generado.
     */
    String generateToken(UserDetails userDetails);
    /**
     * Verifica si un token JWT es valido para los detalles del usuario proporcionados.
     *
     * @param token       El token JWT a validar.
     * @param userDetails Detalles del usuario con los cuales validar el token.
     * @return true si el token es valido, false en caso contrario.
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}

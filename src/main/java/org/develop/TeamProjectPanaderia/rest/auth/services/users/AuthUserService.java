package org.develop.TeamProjectPanaderia.rest.auth.services.users;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
/**
 * Interfaz que extiende {@code UserDetailsService} y proporciona operaciones especificas
 * para el servicio de usuario en la autenticacion.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
public interface AuthUserService extends UserDetailsService {
    /**
     * Carga los detalles del usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario para el cual cargar los detalles del usuario.
     * @return Detalles del usuario cargados.
     */
    @Override
    UserDetails loadUserByUsername(String username);
}

package org.develop.TeamProjectPanaderia.rest.auth.services.users;

import org.develop.TeamProjectPanaderia.rest.auth.repositories.AuthRepository;
import org.develop.TeamProjectPanaderia.rest.users.exceptions.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
/**
 * Implementacion del servicio {@code AuthUserService} que extiende {@code UserDetailsService}
 * y proporciona operaciones especificas para el servicio de usuario en la autenticacion.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Service("userDetailsService")
public class AuthUserServiceImpl implements AuthUserService{
    private final AuthRepository authRepository;

    @Autowired
    public AuthUserServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Carga los detalles del usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario para el cual cargar los detalles del usuario.
     * @return Detalles del usuario cargados.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return authRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("username " + username));
    }
}

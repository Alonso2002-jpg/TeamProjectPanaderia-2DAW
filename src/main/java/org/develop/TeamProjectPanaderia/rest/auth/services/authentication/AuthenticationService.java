package org.develop.TeamProjectPanaderia.rest.auth.services.authentication;

import org.develop.TeamProjectPanaderia.rest.auth.dto.JwtAuthResponseDto;
import org.develop.TeamProjectPanaderia.rest.auth.dto.UserSignInRequest;
import org.develop.TeamProjectPanaderia.rest.auth.dto.UserSignUpRequest;
/**
 * Interfaz que define los metodos para realizar operaciones de autenticacion en el sistema.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
public interface AuthenticationService {

    /**
     * Realiza la operacion de registro (sign-up) de un nuevo usuario.
     *
     * @param signUpRequest La solicitud de registro de usuario que contiene la informacion necesaria.
     * @return Un objeto JwtAuthResponseDto que contiene el token JWT resultante de la operacion de registro.
     */
    JwtAuthResponseDto signUp(UserSignUpRequest signUpRequestRequest);
    /**
     * Realiza la operacion de inicio de sesion (sign-in) de un usuario existente.
     *
     * @param signInRequest La solicitud de inicio de sesion que contiene la informacion necesaria.
     * @return Un objeto JwtAuthResponseDto que contiene el token JWT resultante de la operacion de inicio de sesion.
     */
    JwtAuthResponseDto signIn(UserSignInRequest signInRequest);
}

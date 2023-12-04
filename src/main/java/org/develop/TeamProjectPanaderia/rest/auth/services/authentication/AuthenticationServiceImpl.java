package org.develop.TeamProjectPanaderia.rest.auth.services.authentication;

import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.rest.auth.dto.JwtAuthResponseDto;
import org.develop.TeamProjectPanaderia.rest.auth.dto.UserSignInRequest;
import org.develop.TeamProjectPanaderia.rest.auth.dto.UserSignUpRequest;
import org.develop.TeamProjectPanaderia.rest.auth.exceptions.AuthSignInInvalid;
import org.develop.TeamProjectPanaderia.rest.auth.exceptions.UserInvalidPasswords;
import org.develop.TeamProjectPanaderia.rest.auth.repositories.AuthRepository;
import org.develop.TeamProjectPanaderia.rest.auth.services.jwt.JwtService;
import org.develop.TeamProjectPanaderia.rest.users.exceptions.UsernameOrEmailExists;
import org.develop.TeamProjectPanaderia.rest.users.model.Role;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementacion de la interfaz AuthenticationService que proporciona metodos para realizar operaciones de autenticacion.
 *
 * <p>
 * Esta clase utiliza el repositorio AuthRepository, un codificador de contrasenas (PasswordEncoder),
 * un servicio JWT (JwtService) y un administrador de autenticacion (AuthenticationManager) para llevar a cabo
 * las operaciones de registro (sign-up) e inicio de sesion (sign-in) de usuarios.
 * </p>
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService{
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor que acepta instancias de AuthRepository, PasswordEncoder, JwtService y AuthenticationManager.
     *
     * @param authRepository       Repositorio para acceder a la base de datos de usuarios.
     * @param passwordEncoder      Codificador de contrasenas para cifrar las contrasenas de los usuarios.
     * @param jwtService           Servicio JWT para generar tokens de autenticacion.
     * @param authenticationManager Administrador de autenticacion para realizar la autenticacion de usuarios.
     */
    public AuthenticationServiceImpl(AuthRepository authRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Realiza la operacion de registro (sign-up) de un usuario en el sistema.
     *
     * @param signUpRequestRequest La solicitud de registro del usuario.
     * @return Un objeto JwtAuthResponseDto que contiene el token JWT generado para el usuario registrado.
     * @throws UserInvalidPasswords Si las contrasenas proporcionadas no coinciden.
     * @throws UsernameOrEmailExists Si el nombre de usuario o correo electronico ya existe en la base de datos.
     */
    @Override
    public JwtAuthResponseDto signUp(UserSignUpRequest signUpRequestRequest) {
        log.info("signUp: {}", signUpRequestRequest);
        if (signUpRequestRequest.getPassword().contentEquals(signUpRequestRequest.getPasswordComprobacion())){
            User user = User.builder()
                    .name(signUpRequestRequest.getName())
                    .username(signUpRequestRequest.getUsername())
                    .password(passwordEncoder.encode(signUpRequestRequest.getPassword()))
                    .email(signUpRequestRequest.getEmail())
                    .roles(Stream.of(Role.USER).collect(Collectors.toSet()))
                    .build();
            try {
                var usersaved = authRepository.save(user);
                return JwtAuthResponseDto.builder()
                        .token(jwtService.generateToken(usersaved))
                        .build();
            } catch (DataIntegrityViolationException e){
                throw new UsernameOrEmailExists("Username or email already exists");
            }
        }else {
            throw new UserInvalidPasswords("Passwords don't match");
        }
    }

    /**
     * Realiza la operacion de inicio de sesion (sign-in) de un usuario en el sistema.
     *
     * @param signInRequest La solicitud de inicio de sesion del usuario.
     * @return Un objeto JwtAuthResponseDto que contiene el token JWT generado para el usuario autenticado.
     * @throws AuthSignInInvalid Si el nombre de usuario o la contrasena son incorrectos.
     */
    @Override
    public JwtAuthResponseDto signIn(UserSignInRequest signInRequest) {
        log.info("signIn: {}", signInRequest);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        var user = authRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() -> new AuthSignInInvalid("User or Password incorrect"));
        var jwt = jwtService.generateToken(user);
        return JwtAuthResponseDto.builder().token(jwt).build();
    }
}


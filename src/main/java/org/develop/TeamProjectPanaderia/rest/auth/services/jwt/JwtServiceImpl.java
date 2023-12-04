package org.develop.TeamProjectPanaderia.rest.auth.services.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
/**
 * Implementacion de la interfaz {@code JwtService} que proporciona operaciones para manipular tokens JWT.
 *
 *@author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret}")
    private String jwtSigninKey;
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Extrae el nombre de usuario (username) contenido en un token JWT.
     *
     * @param token El token JWT del cual extraer el nombre de usuario.
     * @return El nombre de usuario extraido del token.
     */
    @Override
    public String extractUserName(String token) {
        log.info("Extracting user name from token");
        return extractClaim(token, DecodedJWT::getSubject);
    }

    /**
     * Genera un token JWT para los detalles del usuario proporcionados.
     *
     * @param userDetails Detalles del usuario para los cuales generar el token JWT.
     * @return El token JWT generado.
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        log.info("Generating token for user " + userDetails.getUsername());
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Verifica si un token JWT es válido para los detalles del usuario proporcionados.
     *
     * @param token       El token JWT a validar.
     * @param userDetails Detalles del usuario con los cuales validar el token.
     * @return {@code true} si el token es valido, {@code false} en caso contrario.
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.info("Validating token for user " + userDetails.getUsername());
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extrae un reclamo (claim) especifico de un token JWT utilizando una función proporcionada.
     *
     * @param token           El token JWT del cual extraer el reclamo.
     * @param claimsResolvers Funcion que proporciona la logica para extraer el reclamo.
     * @param <T>             El tipo de dato del reclamo.
     * @return El reclamo extraido del token.
     */
    private <T> T extractClaim(String token, Function<DecodedJWT, T> claimsResolvers) {
        log.info("Extracting claim from token " + token);
        final DecodedJWT decodedJWT = JWT.decode(token);
        return claimsResolvers.apply(decodedJWT);
    }

    /**
     * Genera un token con datos extra
     *
     * @param extraClaims Datos extra
     * @param userDetails Detalles del usuario
     * @return token
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // Preparamos el token
        Algorithm algorithm = Algorithm.HMAC512(getSigningKey());
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + (1000 * jwtExpiration));

        return JWT.create()
                .withHeader(createHeader())
                .withSubject(userDetails.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .withClaim("extraClaims", extraClaims)
                .sign(algorithm);
    }

    /**
     * Comprueba si el token ha expirado
     *
     * @param token token
     * @return true si ha expirado
     */
    private boolean isTokenExpired(String token) {
        Date expirationDate = extractExpiration(token);
        return expirationDate.before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token
     *
     * @param token token
     * @return fecha de expiración
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, DecodedJWT::getExpiresAt);
    }

    /**
     * Crea el encabezado del token
     *
     * @return encabezado del token
     */
    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        return header;
    }

    /**
     * Obtiene la clave de firma
     *
     * @return clave de firma
     */
    private byte[] getSigningKey() {
        return Base64.getEncoder().encode(jwtSigninKey.getBytes());
    }
}


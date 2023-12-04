package org.develop.TeamProjectPanaderia.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.rest.auth.services.jwt.JwtService;
import org.develop.TeamProjectPanaderia.rest.auth.services.users.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/**
 * Este filtro se encarga de la autenticacion mediante JWT.
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AuthUserService authUserService;

    /**
     * Constructor de la clase JwtAuthenticationFilter.
     *
     * @param jwtService      Servicio para la manipulacion de JWT.
     * @param authUserService Servicio para la obtencion de detalles del usuario.
     */
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, AuthUserService authUserService) {
        this.jwtService = jwtService;
        this.authUserService = authUserService;
    }

    /**
     * Metodo para realizar la autenticacion basada en JWT.
     *
     * @param request     Objeto HttpServletRequest.
     * @param response    Objeto HttpServletResponse.
     * @param filterChain Cadena de filtros a aplicar.
     * @throws ServletException Si hay un error en el servlet.
     * @throws IOException      Si hay un error de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Starting JWT authentication filter...");
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        UserDetails userDetails;
        String username;

        if (!StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader,"Bearer ")) {
            log.info("No Authentication header found");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Authentication header found");

        jwt = authHeader.substring(7);

        try {
            username = jwtService.extractUserName(jwt);
        }catch (Exception e) {
            log.info("Invalid JWT token");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        }

        log.info("Authenticating user: {}", username);
        if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("Checking JWT token and username");
                try {
                    userDetails = authUserService.loadUserByUsername(username);
                }catch (Exception e) {
                    log.info("User Not Found: {}", username);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User Not Authorized");
                    return;
                }

                log.info("User found: {}", username);
                if (jwtService.isTokenValid(jwt,userDetails)){
                    log.info("Valid JWT token");
                    SecurityContext context = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken authToke = new UsernamePasswordAuthenticationToken(
                            userDetails,null,userDetails.getAuthorities()
                    );
                    authToke.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToke);

                    SecurityContextHolder.setContext(context);
                }

            filterChain.doFilter(request, response);
        }
    }
}

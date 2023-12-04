package org.develop.TeamProjectPanaderia.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * Clase de configuración para la gestión de configuración CORS (Cross-Origin Resource Sharing).
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Configuration
public class CorsConfig {

    /**
     * Configura las opciones CORS para permitir solicitudes desde dominios diferentes.
     *
     * @return El WebMvcConfigurer configurado para manejar CORS.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/rest/producto/**")
                        .allowedMethods("*")
                        .maxAge(3600);
            }

        };
    }
}
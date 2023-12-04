package org.develop.TeamProjectPanaderia.config.storage;

import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.storage.services.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Clase de configuracion para la inicializacion y configuracion del servicio de almacenamiento.
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Configuration
@Slf4j
public class StorageConfig {
    /**
     * Configura un CommandLineRunner para inicializar y configurar el servicio de almacenamiento.
     *
     * @param storageService El servicio de almacenamiento a inicializar.
     * @param deleteAll      La propiedad que indica si se deben borrar todos los archivos al inicio.
     * @return Un CommandLineRunner para ejecutar las acciones de inicializacion.
     */
    @Bean
    public CommandLineRunner init(StorageService storageService, @Value("${upload.delete") String deleteAll) {
        return args -> {
            if (deleteAll.equals("true")) {
                log.info("Borrando Archivos de Almacenamiento");
                storageService.deleteAll();
            }

            storageService.init();
        };
    }
}

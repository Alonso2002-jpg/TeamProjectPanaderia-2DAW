package org.develop.TeamProjectPanaderia.storage.services;

import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.storage.controllers.StorageController;
import org.develop.TeamProjectPanaderia.storage.exceptions.StorageBadRequestException;
import org.develop.TeamProjectPanaderia.storage.exceptions.StorageInternalException;
import org.develop.TeamProjectPanaderia.storage.exceptions.StorageNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

/**
 * Servicio de almacenamiento basado en sistema de archivos.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Service
@Slf4j
public class FileSystemStorageService implements StorageService{
    private final Path rootLocation;
    private final List<String> allowedExtensions = List.of("png", "jpg", "jpeg", "gif");


    /**
     * Constructor del servicio FileSystemStorageService.
     *
     * @param rootLocation La ubicacion raiz del almacenamiento.
     */
    public FileSystemStorageService(@Value("${upload.root-location}") String rootLocation){

        this.rootLocation = Paths.get(rootLocation);
    }
    /**
     * Inicializa el almacenamiento creando el directorio raiz si no existe.
     */
    @Override
    public void init() {
        log.info("Inicializando almacenamiento");
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageInternalException("No se puede inicializar el almacenamiento " + e);
        }
    }

    /**
     * Almacena un archivo en el sistema de archivos.
     *
     * @param file El archivo a almacenar.
     * @return El nombre del archivo almacenado.
     */
    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(filename);
        allowedExtensions.stream()
                .filter(ex -> extension.contains(ex))
                .findAny()
                .orElseThrow(()-> new StorageBadRequestException("Extension no permitida"));
        String justFilename = filename.replace("." + extension, "");
        String storedFilename = System.currentTimeMillis() + "_" + justFilename + "." + extension;

        try {
            if (file.isEmpty()) {
                throw new StorageBadRequestException("Fichero vacío " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageBadRequestException(
                        "No se puede almacenar un fichero con una ruta relativa fuera del directorio actual "
                                + filename);
            }

            try (InputStream inputStream = file.getInputStream()) {
                log.info("Almacenando fichero " + filename + " como " + storedFilename);
                Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                        StandardCopyOption.REPLACE_EXISTING);
                return storedFilename;
            }

        } catch (IOException e) {
            throw new StorageInternalException("Fallo al almacenar fichero " + filename + " " + e);
        }
    }


    /**
     * Carga todos los archivos almacenados en el sistema de archivos.
     *
     * @return Una secuencia de rutas de archivos almacenados.
     */
    @Override
    public Stream<Path> loadAll() {
        log.info("Cargando todos los ficheros almacenados");
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageInternalException("Fallo al leer ficheros almacenados " + e);
        }
    }

    /**
     * Carga un archivo específico del sistema de archivos.
     *
     * @param filename El nombre del archivo a cargar.
     * @return La ruta del archivo cargado.
     */
    @Override
    public Path load(String filename) {
        log.info("Cargando fichero " + filename);
        return rootLocation.resolve(filename);
    }

    /**
     * Carga un archivo como recurso desde el sistema de archivos.
     *
     * @param filename El nombre del archivo a cargar como recurso.
     * @return El recurso del archivo cargado.
     */
    @Override
    public Resource loadAsResource(String filename) {
        log.info("Cargando fichero " + filename);
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageNotFoundException("No se puede leer fichero: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageNotFoundException("No se puede leer fichero: " + filename + " " + e);
        }
    }

    /**
     * Elimina un archivo especifico del sistema de archivos.
     *
     * @param filename El nombre del archivo a eliminar.
     */
    @Override
    public void delete(String filename) {
        String justFilename = StringUtils.getFilename(filename);
        try {
            log.info("Eliminando fichero " + filename);
            Path file = load(justFilename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageInternalException("No se puede eliminar el fichero " + filename + " " + e);
        }
    }

    /**
     * Elimina todos los archivos almacenados en el sistema de archivos.
     */
    @Override
    public void deleteAll() {
        log.info("Eliminando todos los ficheros almacenados");
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * Obtiene la URL de un archivo especifico del sistema de archivos.
     *
     * @param filename El nombre del archivo del cual obtener la URL.
     * @return La URL del archivo.
     */
    @Override
    public String getUrl(String filename) {
        log.info("Obteniendo URL del fichero " + filename);
        return MvcUriComponentsBuilder
                // El segundo argumento es necesario solo cuando queremos obtener la imagen
                // En este caso tan solo necesitamos obtener la URL
                .fromMethodName(StorageController.class, "serveFile", filename, null)
                .build().toUriString();
    }
}

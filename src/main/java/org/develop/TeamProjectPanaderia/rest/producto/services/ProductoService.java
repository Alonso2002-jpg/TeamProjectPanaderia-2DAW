package org.develop.TeamProjectPanaderia.rest.producto.services;

import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.rest.producto.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * La interfaz {@code ProductoService} proporciona operaciones de servicio para la gestión de productos.
 *
 * @version 1.0
 */
public interface ProductoService {

    /**
     * Recupera una página de productos según los criterios de búsqueda proporcionados.
     *
     * @param nombre    Nombre del producto (opcional).
     * @param stockMin  Cantidad mínima del producto (opcional).
     * @param precioMax Precio máximo del producto (opcional).
     * @param isActivo  Indica si el producto está activo (opcional).
     * @param categoria Nombre de la categoría del producto (opcional).
     * @param proveedor NIF del proveedor del producto (opcional).
     * @param pageable  Información de paginación y ordenación.
     * @return Una página de productos que cumplen con los criterios de búsqueda.
     */
    Page<Producto> findAll(Optional<String> nombre, Optional <Integer> stockMin, Optional<Double> precioMax, Optional<Boolean> isActivo, Optional<String> categoria, Optional<String> proveedor, Pageable pageable);

    /**
     * Recupera un producto por su identificador único.
     *
     * @param id Identificador único del producto.
     * @return El producto si se encuentra, o null si no se encuentra.
     */
    Producto findById(String id);

    /**
     * Busca un producto por su nombre.
     *
     * @param name Nombre del producto a buscar.
     * @return El producto si se encuentra, o null si no se encuentra.
     */
    Producto findByName(String name);

    /**
     * Guarda un nuevo producto en la base de datos.
     *
     * @param productoCreateDto Datos del producto a crear.
     * @return El producto creado.
     */
    Producto save(ProductoCreateDto productoCreateDto);

    /**
     * Actualiza un producto existente en la base de datos.
     *
     * @param id                Identificador único del producto a actualizar.
     * @param productoUpdateDto Datos actualizados del producto.
     * @return El producto actualizado.
     */
    Producto update(String id, ProductoUpdateDto productoUpdateDto);

    /**
     * Actualiza la imagen de un producto existente.
     *
     * @param id   Identificador único del producto.
     * @param file Archivo de imagen a subir.
     * @return El producto con la nueva imagen.
     */
    Producto updateImg(String id, MultipartFile file);

    /**
     * Elimina un producto por su identificador único.
     *
     * @param id Identificador único del producto a eliminar.
     */
    void deleteById(String id);
}


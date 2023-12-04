package org.develop.TeamProjectPanaderia.rest.proveedores.services;

import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interfaz que define operaciones relacionadas con la gestión de proveedores en la aplicación.
 */
public interface ProveedorService {

    /**
     * Guarda un proveedor en la base de datos.
     *
     * @param proveedores Objeto DTO que contiene la información del proveedor a guardar.
     * @return El proveedor guardado.
     */
    Proveedor saveProveedores(ProveedorCreateDto proveedores);

    /**
     * Actualiza un proveedor en la base de datos.
     *
     * @param proveedorUpdateDto Objeto DTO que contiene la información actualizada del proveedor.
     * @param id Identificador único del proveedor que se va a actualizar.
     * @return El proveedor actualizado.
     */
    Proveedor updateProveedor(ProveedorUpdateDto proveedorUpdateDto, Long id);

    /**
     * Obtiene un proveedor por su identificador único.
     *
     * @param id Identificador único del proveedor.
     * @return El proveedor encontrado.
     */
    Proveedor getProveedoresById(Long id);

    /**
     * Elimina un proveedor por su identificador único.
     *
     * @param id Identificador único del proveedor que se va a eliminar.
     */
    void deleteProveedoresById(Long id);

    /**
     * Busca un proveedor por su NIF.
     *
     * @param nif NIF del proveedor a buscar.
     * @return El proveedor encontrado.
     */
    Proveedor findProveedoresByNIF(String nif);

    /**
     * Obtiene una página de proveedores que cumplen con ciertos criterios.
     *
     * @param nif NIF del proveedor (opcional).
     * @param name Nombre del proveedor (opcional).
     * @param isActive Estado de actividad del proveedor (opcional).
     * @param tipo Tipo del proveedor (opcional).
     * @param pageable Información de paginación y ordenamiento.
     * @return Página de proveedores que cumplen con los criterios especificados.
     */
    Page<Proveedor> findAll(Optional<String> nif, Optional<String> name, Optional<Boolean> isActive,Optional<String> tipo, Pageable pageable);
}

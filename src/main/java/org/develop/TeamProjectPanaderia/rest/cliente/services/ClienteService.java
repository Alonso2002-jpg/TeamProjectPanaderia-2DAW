package org.develop.TeamProjectPanaderia.rest.cliente.services;


import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Direccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define los métodos de servicio para la entidad Cliente.
 */
public interface ClienteService {
    /**
     * Obtiene una página de clientes según los criterios proporcionados.
     *
     * @param nombreCompleto Nombre completo del cliente (opcional).
     * @param categoria      Categoría del cliente (opcional).
     * @param pageable       Objeto Pageable para la paginación y ordenación de resultados.
     * @return Una página de clientes que cumple con los criterios especificados.
     */
    Page<Cliente> findAll(Optional<String> nombreCompleto, Optional<String> categoria, Pageable pageable);
    /**
     * Busca un cliente por su identificador único (ID).
     *
     * @param id El ID del cliente a buscar.
     * @return El cliente encontrado.
     */
    Cliente findById(Long id);
    /**
     * Busca un cliente por su número de identificación (DNI).
     *
     * @param dni El número de identificación (DNI) del cliente a buscar.
     * @return El cliente encontrado.
     */
    Cliente findByDni(String dni);
    /**
     * Guarda un nuevo cliente en la base de datos.
     *
     * @param clienteCreateDto Datos del cliente a crear.
     * @return El cliente creado.
     */
    Cliente save(ClienteCreateDto clienteCreateDto);
    /**
     * Actualiza un cliente existente en la base de datos.
     *
     * @param id                El ID del cliente a actualizar.
     * @param clienteUpdateDto Datos actualizados del cliente.
     * @return El cliente actualizado.
     */
    Cliente update(Long id, ClienteUpdateDto clienteUpdateDto);
    /**
     * Actualiza la imagen de un cliente existente.
     *
     * @param id   El ID del cliente cuya imagen se actualizará.
     * @param file Archivo de imagen a utilizar para la actualización.
     * @return El cliente con la imagen actualizada.
     */
    Cliente updateImg(Long id, MultipartFile file);
    /**
     * Actualiza la dirección de un cliente existente.
     *
     * @param id       El ID del cliente cuya dirección se actualizará.
     * @param direccion La nueva dirección del cliente.
     * @return El cliente con la dirección actualizada.
     */
    Cliente updateDireccion(Long id, Direccion direccion);
    /**
     * Elimina un cliente por su identificador único (ID).
     *
     * @param id El ID del cliente a eliminar.
     */
    void deleteById(Long id);
    /**
     * Busca todos los clientes que tengan el estado activo especificado.
     *
     * @param isActive Estado de activación que se utilizará como criterio de búsqueda.
     * @return Una lista de clientes que cumplen con el criterio de activación especificado.
     */
    List<Cliente> findByActiveIs(Boolean isActive);

}

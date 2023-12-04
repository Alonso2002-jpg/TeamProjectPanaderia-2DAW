package org.develop.TeamProjectPanaderia.rest.proveedores.mapper;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorResponseDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Componente encargado de realizar la conversión entre objetos DTO y entidades de proveedores.
 * Proporciona métodos para convertir objetos ProveedorCreateDto, ProveedorUpdateDto y Proveedor
 * a sus respectivas entidades y DTOs, así como para crear listas y páginas de DTOs a partir
 * de listas y páginas de entidades de proveedores.
 */
@Component
public class ProveedorMapper {

    /**
     * Convierte un objeto ProveedorCreateDto y una entidad Categoria a una entidad Proveedor.
     *
     * @param proveedor El DTO de creación del proveedor.
     * @param categoria La entidad de la categoría del proveedor.
     * @return La entidad del proveedor convertida.
     */
    public Proveedor toProveedor(ProveedorCreateDto proveedor, Categoria categoria){
        return Proveedor.builder()
                .nif(proveedor.getNif())
                .tipo(categoria)
                .numero(proveedor.getNumero())
                .nombre(proveedor.getNombre())
                .build();
    }

    /**
     * Convierte una entidad Proveedor a un objeto ProveedorCreateDto.
     *
     * @param proveedor La entidad del proveedor.
     * @return El DTO de creación del proveedor convertido.
     */
    public ProveedorCreateDto ToCreate(Proveedor proveedores){
        return ProveedorCreateDto.builder()
                .nif(proveedores.getNif())
                .tipo(proveedores.getTipo().toString())
                .numero(proveedores.getNumero())
                .nombre(proveedores.getNombre())
                .build();

    }

    /**
     * Convierte un objeto ProveedorUpdateDto, una entidad Proveedor y una entidad Categoria
     * a una entidad Proveedor actualizada.
     *
     * @param proveedorUpd El DTO de actualización del proveedor.
     * @param proveedor La entidad del proveedor antes de la actualización.
     * @param categoria La entidad de la categoría del proveedor (puede ser nula).
     * @return La entidad del proveedor actualizada.
     */
    public Proveedor toProveedor(ProveedorUpdateDto proveedorUpd, Proveedor proveedor, Categoria categoria){
        return Proveedor.builder()
                .id(proveedor.getId())
                .nif(proveedorUpd.getNif() == null ? proveedor.getNif() : proveedorUpd.getNif())
                .tipo(categoria == null ? proveedor.getTipo() : categoria)
                .numero(proveedorUpd.getNumero() == null ? proveedor.getNumero() : proveedorUpd.getNumero())
                .nombre(proveedorUpd.getNombre() == null ? proveedor.getNombre() : proveedorUpd.getNombre())
                .isActive(proveedorUpd.getIsActive() == null ? proveedor.getIsActive() : proveedorUpd.getIsActive())
                .fechaUpdate(LocalDate.now())
                .build();
    }

    /**
     * Convierte una entidad Proveedor a un objeto ProveedorUpdateDto.
     *
     * @return El DTO de actualización del proveedor convertido.
     */
    public ProveedorUpdateDto ToUpdate(Proveedor proveedores){
        return ProveedorUpdateDto.builder()
                .nif(proveedores.getNif())
                .tipo(proveedores.getTipo().toString())
                .numero(proveedores.getNumero())
                .nombre(proveedores.getNombre())
                .build();
    }

    /**
     * Convierte una entidad Proveedor a un objeto ProveedorResponseDto.
     *
     * @return El DTO de respuesta del proveedor convertido.
     */
    public ProveedorResponseDto toResponse(Proveedor proveedores){
        return ProveedorResponseDto.builder()
                .nif(proveedores.getNif())
                .fechaUpdate(proveedores.getFechaUpdate())
                .fechaCreacion(proveedores.getFechaCreacion())
                .tipo(proveedores.getTipo())
                .numero(proveedores.getNumero())
                .nombre(proveedores.getNombre())
                .isActive(proveedores.getIsActive())
                .build();
    }

    /**
     * Convierte una lista de entidades Proveedor a una lista de objetos ProveedorResponseDto.
     *
     * @param proveedores La lista de entidades de proveedores.
     * @return La lista de DTOs de respuesta de proveedores convertida.
     */
    public List<ProveedorResponseDto> toResponse(List<Proveedor> proveedores){
        return proveedores.stream().map(this::toResponse).toList();
    }

    /**
     * Convierte una página de entidades Proveedor a una página de objetos ProveedorResponseDto.
     *
     * @param proveedores La página de entidades de proveedores.
     * @return La página de DTOs de respuesta de proveedores convertida.
     */
    public Page<ProveedorResponseDto> toPageResponse(Page<Proveedor> proveedores){
        return proveedores.map(this::toResponse);
    }
}

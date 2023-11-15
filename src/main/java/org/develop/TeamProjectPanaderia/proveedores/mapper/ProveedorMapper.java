package org.develop.TeamProjectPanaderia.proveedores.mapper;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorResponseDto;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ProveedorMapper {

    public Proveedor toProveedor(ProveedorCreateDto proveedor, Categoria categoria){
        return Proveedor.builder()
                .nif(proveedor.getNif())
                .tipo(categoria)
                .numero(proveedor.getNumero())
                .nombre(proveedor.getNombre())
                .build();
    }

    public ProveedorCreateDto ToCreate(Proveedor proveedores){
        return ProveedorCreateDto.builder()
                .nif(proveedores.getNif())
                .tipo(proveedores.getTipo().toString())
                .numero(proveedores.getNumero())
                .nombre(proveedores.getNombre())
                .build();

    }

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

    public ProveedorUpdateDto ToUpdate(Proveedor proveedores){
        return ProveedorUpdateDto.builder()
                .nif(proveedores.getNif())
                .tipo(proveedores.getTipo().toString())
                .numero(proveedores.getNumero())
                .nombre(proveedores.getNombre())
                .build();
    }
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

    public List<ProveedorResponseDto> toResponse(List<Proveedor> proveedores){
        return proveedores.stream().map(this::toResponse).toList();
    }

    public Page<ProveedorResponseDto> toPageResponse(Page<Proveedor> proveedores){
        return proveedores.map(this::toResponse);
    }
}

package org.develop.TeamProjectPanaderia.proveedores.mapper;

import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorResponseDto;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProveedorMapper {

    public ProveedorResponseDto toResponse(Proveedor proveedores){
        return new ProveedorResponseDto(
                proveedores.getId(),
                proveedores.getNIF(),
                proveedores.getTipo(),
                proveedores.getNumero(),
                proveedores.getNombre(),
                proveedores.getFechaCreacion(),
                proveedores.getFechaUpdate()
        );
    }

    public List<ProveedorResponseDto> toResponse(List<Proveedor> proveedores){
        return proveedores.stream().map(this::toResponse).toList();
    }

    public ProveedorCreateDto ToCreate(Proveedor proveedores){
        return new ProveedorCreateDto(
                proveedores.getId(),
                proveedores.getNIF(),
                proveedores.getTipo(),
                proveedores.getNumero(),
                proveedores.getNombre(),
                proveedores.getFechaCreacion(),
                proveedores.getFechaUpdate()
        );
    }

    public List<ProveedorCreateDto> ToCreate(List<Proveedor> proveedores){
        return proveedores.stream().map(this::ToCreate).toList();
    }

    public ProveedorUpdateDto ToUpdate(Proveedor proveedores){
        return new ProveedorUpdateDto(
                proveedores.getId(),
                proveedores.getNIF(),
                proveedores.getTipo(),
                proveedores.getNumero(),
                proveedores.getNombre(),
                proveedores.getFechaCreacion(),
                proveedores.getFechaUpdate()
        );
    }

    public List<ProveedorUpdateDto> ToUpdate(List<Proveedor> proveedores){
        return proveedores.stream().map(this::ToUpdate).toList();
    }
}

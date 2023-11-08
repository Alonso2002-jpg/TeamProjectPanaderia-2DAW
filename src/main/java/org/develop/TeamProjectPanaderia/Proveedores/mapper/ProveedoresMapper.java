package org.develop.TeamProjectPanaderia.Proveedores.mapper;

import org.develop.TeamProjectPanaderia.Proveedores.dto.ProveedoresCreateDto;
import org.develop.TeamProjectPanaderia.Proveedores.dto.ProveedoresResponseDto;
import org.develop.TeamProjectPanaderia.Proveedores.dto.ProveedoresUpdateDto;
import org.develop.TeamProjectPanaderia.Proveedores.models.Proveedores;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProveedoresMapper {

    public ProveedoresResponseDto toResponse(Proveedores proveedores){
        return new ProveedoresResponseDto(
                proveedores.getId(),
                proveedores.getNIF(),
                proveedores.getTipo(),
                proveedores.getNumero(),
                proveedores.getNombre(),
                proveedores.getFechaCreacion(),
                proveedores.getFechaUpdate()
        );
    }

    public List<ProveedoresResponseDto> toResponse(List<Proveedores> proveedores){
        return proveedores.stream().map(this::toResponse).toList();
    }

    public ProveedoresCreateDto ToCreate(Proveedores proveedores){
        return new ProveedoresCreateDto(
                proveedores.getId(),
                proveedores.getNIF(),
                proveedores.getTipo(),
                proveedores.getNumero(),
                proveedores.getNombre(),
                proveedores.getFechaCreacion(),
                proveedores.getFechaUpdate()
        );
    }

    public List<ProveedoresCreateDto> ToCreate(List<Proveedores> proveedores){
        return proveedores.stream().map(this::ToCreate).toList();
    }

    public ProveedoresUpdateDto ToUpdate(Proveedores proveedores){
        return new ProveedoresUpdateDto(
                proveedores.getId(),
                proveedores.getNIF(),
                proveedores.getTipo(),
                proveedores.getNumero(),
                proveedores.getNombre(),
                proveedores.getFechaCreacion(),
                proveedores.getFechaUpdate()
        );
    }

    public List<ProveedoresUpdateDto> ToUpdate(List<Proveedores> proveedores){
        return proveedores.stream().map(this::ToUpdate).toList();
    }
}

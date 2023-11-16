package org.develop.TeamProjectPanaderia.rest.cliente.mapper;


import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteResponseDto;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ClienteMapper {

    public Cliente toCliente(ClienteCreateDto dto, Categoria categoria) {
        return Cliente.builder()
                .id(null)
                .nombreCompleto(dto.getNombreCompleto())
                .correo(dto.getCorreo())
                .dni(dto.getDni())
                .telefono(dto.getTelefono() != null ? dto.getTelefono() : "")
                .imagen(dto.getImagen() != null ? dto.getImagen() : Cliente.IMAGE_DEFAULT)
                .categoria(categoria)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
    }

    public Cliente toCliente(ClienteUpdateDto dto, Cliente cliente, Categoria categoria) {
        return Cliente.builder()
                .id(cliente.getId())
                .nombreCompleto(dto.getNombreCompleto() != null ? dto.getNombreCompleto() : cliente.getNombreCompleto())
                .correo(dto.getCorreo() != null ? dto.getCorreo() : cliente.getCorreo())
                .dni(cliente.getDni())
                .telefono(dto.getTelefono() != null ? dto.getTelefono() : cliente.getTelefono())
                .imagen(dto.getImagen() != null ? dto.getImagen() : cliente.getImagen())
                .fechaCreacion(cliente.getFechaCreacion())
                .fechaActualizacion(LocalDateTime.now())
                .categoria(categoria)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : cliente.getIsActive())
                .build();
    }

    public ClienteResponseDto toClienteResponseDto(Cliente cliente) {
        return ClienteResponseDto.builder()
                .id(cliente.getId())
                .nombreCompleto(cliente.getNombreCompleto())
                .correo(cliente.getCorreo())
                .dni(cliente.getDni())
                .telefono(cliente.getTelefono())
                .imagen(cliente.getImagen())
                .fechaCreacion(cliente.getFechaCreacion())
                .fechaActualizacion(cliente.getFechaActualizacion())
                .categoria(cliente.getCategoria().getNameCategory())
                .isActive(cliente.getIsActive())
                .build();
    }

}





package org.develop.TeamProjectPanaderia.cliente.mapper;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteCreateDto;

import org.develop.TeamProjectPanaderia.cliente.dto.ClienteResponseDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.hibernate.validator.constraints.Length;
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
                .telefono(dto.getTelefono())
                .producto(dto.getProducto())
                .categoria(categoria)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();
    }

    public Cliente toCliente(ClienteUpdateDto dto, Cliente cliente, Categoria categoria, String producto) {
        return Cliente.builder()
                .id(cliente.getId())
                .nombreCompleto(dto.getNombreCompleto() != null ? dto.getNombreCompleto() : cliente.getNombreCompleto())
                .correo(dto.getCorreo() != null ? dto.getCorreo() : cliente.getCorreo())
                .dni(cliente.getDni())
                .telefono(dto.getTelefono() != null ? dto.getTelefono() : cliente.getTelefono())
                .producto(producto) // PROVISIONAL
                .fechaCreacion(cliente.getFechaCreacion())
                .fechaActualizacion(LocalDateTime.now())
                .categoria(categoria)
                .build();
    }

    public ClienteResponseDto toClienteResponseDto(Cliente cliente) {
        return ClienteResponseDto.builder()
                .id(cliente.getId())
                .nombreCompleto(cliente.getNombreCompleto())
                .correo(cliente.getCorreo())
                .dni(cliente.getDni())
                .telefono(cliente.getTelefono())
                .producto(cliente.getProducto())
                .fechaCreacion(cliente.getFechaCreacion())
                .fechaActualizacion(cliente.getFechaActualizacion())
                .categoria(cliente.getCategoria().getNameCategory())
                .build();
    }
}





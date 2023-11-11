package org.develop.TeamProjectPanaderia.cliente.mapper;


import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteResponseDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ClienteMapper {

    public Cliente toCliente(ClienteCreateDto dto, Categoria categoria,Producto producto) {
        return Cliente.builder()
                .id(null)
                .nombreCompleto(dto.getNombreCompleto())
                .correo(dto.getCorreo())
                .dni(dto.getDni())
                .telefono(dto.getTelefono())
                .imagen(dto.getImagen())
                .producto(producto)
                .categoria(categoria)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();
    }

    public Cliente toCliente(ClienteUpdateDto dto, Cliente cliente, Categoria categoria, Producto producto) {
        return Cliente.builder()
                .id(cliente.getId())
                .nombreCompleto(dto.getNombreCompleto() != null ? dto.getNombreCompleto() : cliente.getNombreCompleto())
                .correo(dto.getCorreo() != null ? dto.getCorreo() : cliente.getCorreo())
                .dni(cliente.getDni())
                .telefono(dto.getTelefono() != null ? dto.getTelefono() : cliente.getTelefono())
                .imagen(dto.getImagen() != null ? dto.getImagen() : cliente.getImagen())
                .fechaCreacion(cliente.getFechaCreacion())
                .fechaActualizacion(LocalDateTime.now())
                .producto(producto)
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
                .imagen(cliente.getImagen())
                .fechaCreacion(cliente.getFechaCreacion())
                .fechaActualizacion(cliente.getFechaActualizacion())
                .producto(cliente.getProducto().getNombre())
                .categoria(cliente.getCategoria().getNameCategory())
                .build();
    }
}





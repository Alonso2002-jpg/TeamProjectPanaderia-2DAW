package org.develop.TeamProjectPanaderia.WebSockets.mapper;

import org.develop.TeamProjectPanaderia.WebSockets.dto.NotificacionResponseDto;
import org.develop.TeamProjectPanaderia.categoria.mapper.CategoriaMapper;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.cliente.mapper.ClienteMapper;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.personal.models.Personal;
import org.develop.TeamProjectPanaderia.producto.mapper.ProductoMapper;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.develop.TeamProjectPanaderia.proveedores.mapper.ProveedoresMapper;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedores;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {
    private final ProductoMapper productoMapper;
    private final CategoriaMapper categoriaMapper;
    private final ClienteMapper clienteMapper;
    private final ProveedoresMapper proveedoresMapper;
    private final PersonalMapper personalMapper;

    public NotificacionMapper(ProductoMapper productoMapper,
                              CategoriaMapper categoriaMapper,
                              ClienteMapper clienteMapper,
                              ProveedoresMapper proveedoresMapper,
                              PersonalMapper personalMapper) {
        this.productoMapper = productoMapper;
        this.categoriaMapper = categoriaMapper;
        this.clienteMapper = clienteMapper;
        this.proveedoresMapper = proveedoresMapper;
        this.personalMapper = personalMapper;
                              }

    public NotificacionResponseDto getNotificacionResponseDto(Object obj){
        if (obj.getClass().equals(Categoria.class)){
            return NotificacionResponseDto.builder()
                    .entity("Categoria")
                    .data(categoriaMapper.toResponse((Categoria) obj).toString())
                    .build();
        }else if (obj.getClass().equals(Producto.class)){
            return NotificacionResponseDto.builder()
                    .entity("Producto")
                    .data(productoMapper.toProductoResponseDto((Producto) obj).toString())
                    .build();
        }else if (obj.getClass().equals(Personal.class)){
            return NotificacionResponseDto.builder()
                    .entity("Personal")
                    .data(personalMapper.toPersonalCreateDto((Personal) obj).toString())
                    .build();
        }else if (obj.getClass().equals(Cliente.class)){
            return NotificacionResponseDto.builder()
                    .entity("Cliente")
                    .data(clienteMapper.toClienteResponseDto((Cliente) obj).toString())
                    .build();
        }else if (obj.getClass().equals(Proveedores.class)){
            return NotificacionResponseDto.builder()
                    .entity("Proveedores")
                    .data(proveedoresMapper.toResponse((Proveedores) obj).toString())
                    .build();
        }else{
            throw new IllegalArgumentException("Objeto no encontrado");
        }
    }
}

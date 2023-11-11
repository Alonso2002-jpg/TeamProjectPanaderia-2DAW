package org.develop.TeamProjectPanaderia.WebSockets.mapper;

import org.develop.TeamProjectPanaderia.WebSockets.dto.NotificacionResponseDto;
import org.develop.TeamProjectPanaderia.WebSockets.exceptions.ObjectNotiNotFoundException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {
    private final ProductoMapper productoMapper;
    private final CategoriaMapper categoriaMapper;
    private final ClienteMapper clienteMapper;
    private final ProveedoresMapper proveedoresMapper;
    private final PersonalMapper personalMapper;

    @Autowired
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
        if (obj instanceof Categoria categoria){
            return getNotificacionFromCategoria(categoria);
        }else if (obj instanceof Producto producto){
            return getNotificacionFromProducto(producto);
        }else if (obj instanceof Personal personal){
            return getNotificacionFromPersonal(personal);
        }else if (obj instanceof Cliente cliente){
            return getNotificacionFromCliente(cliente);
        }else if (obj instanceof Proveedores proveedores){
            return getNotificacionFromProveedor(proveedores);
        }else{
            throw new ObjectNotiNotFoundException("Mala Conversion, Objeto no encontrado");
        }
    }

    private NotificacionResponseDto getNotificacionFromProveedor(Proveedores obj) {
        return NotificacionResponseDto.builder()
                .entity("Proveedores")
                .data(proveedoresMapper.toResponse(obj).toString())
                .build();
    }

    private NotificacionResponseDto getNotificacionFromCliente(Cliente obj) {
        return NotificacionResponseDto.builder()
                .entity("Cliente")
                .data(clienteMapper.toClienteResponseDto(obj).toString())
                .build();
    }

    private NotificacionResponseDto getNotificacionFromPersonal(Personal obj) {
        return NotificacionResponseDto.builder()
                .entity("Personal")
                .data(personalMapper.toPersonalCreateDto(obj).toString())
                .build();
    }

    private NotificacionResponseDto getNotificacionFromProducto(Producto obj) {
        return NotificacionResponseDto.builder()
                .entity("Producto")
                .data(productoMapper.toProductoResponseDto(obj).toString())
                .build();
    }

    private NotificacionResponseDto getNotificacionFromCategoria(Categoria obj) {
        return NotificacionResponseDto.builder()
                .entity("Categoria")
                .data(categoriaMapper.toResponse(obj).toString())
                .build();
    }

}

package org.develop.TeamProjectPanaderia.rest.cliente.mapper;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteResponseDto;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Direccion;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
/**
 * {@code ClienteMapper} es una clase que se encarga de mapear entre objetos de tipo Cliente, ClienteCreateDto,
 * ClienteUpdateDto y ClienteResponseDto, así como realizar operaciones de mapeo a nivel de página.
 *
 * @Component Indica que esta clase es un componente de Spring y debe ser escaneada y administrada por el contenedor de Spring.
 */
@Component
public class ClienteMapper {
   private final Gson gson = new GsonBuilder().create();
    /**
     * Convierte un objeto de tipo ClienteCreateDto junto con una categoría a un objeto de tipo Cliente.
     *
     * @param dto       ClienteCreateDto con los datos del cliente a crear.
     * @param categoria La categoría asociada al cliente.
     * @return Objeto de tipo Cliente creado a partir de los datos proporcionados.
     */
    public Cliente toCliente(ClienteCreateDto dto, Categoria categoria) {
        return Cliente.builder()
                .id(null)
                .nombreCompleto(dto.getNombreCompleto())
                .correo(dto.getCorreo())
                .dni(dto.getDni())
                .telefono(dto.getTelefono() != null ? dto.getTelefono() : "")
                .imagen(dto.getImagen() != null ? dto.getImagen() : Cliente.IMAGE_DEFAULT)
                .categoria(categoria)
                .direccion(toJson(dto.getDireccion()))
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
    }
    /**
     * Convierte un objeto de tipo ClienteUpdateDto junto con un cliente existente y una categoría a un objeto de tipo Cliente.
     *
     * @param dto       ClienteUpdateDto con los datos del cliente a actualizar.
     * @param cliente   Cliente existente que se está actualizando.
     * @param categoria La categoría asociada al cliente.
     * @return Objeto de tipo Cliente actualizado a partir de los datos proporcionados.
     */
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
                .direccion(dto.getDireccion() == null ? cliente.getDireccion() : toJson(dto.getDireccion()))
                .categoria(categoria)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : cliente.getIsActive())
                .build();
    }
    /**
     * Convierte un objeto de tipo Cliente a un objeto de tipo ClienteResponseDto.
     *
     * @param cliente Cliente del cual se obtendrán los datos de respuesta.
     * @return Objeto de tipo ClienteResponseDto con los datos del cliente.
     */
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
                .direccion(gson.fromJson(cliente.getDireccion(), new TypeToken<Direccion>(){}.getType()))
                .categoria(cliente.getCategoria().getNameCategory())
                .isActive(cliente.getIsActive())
                .build();
    }
    /**
     * Convierte una página de objetos de tipo Cliente a una página de objetos de tipo ClienteResponseDto.
     *
     * @param clientePage Página de objetos de tipo Cliente.
     * @return Página de objetos de tipo ClienteResponseDto.
     */
    public Page<ClienteResponseDto> toPageClienteResponse(Page<Cliente> clientePage){
        return clientePage.map(this::toClienteResponseDto);
    }
    /**
     * Convierte un objeto de tipo Direccion a su representación JSON.
     *
     * @param direccion Objeto de tipo Direccion.
     * @return Representación JSON de la dirección.
     */
    public String toJson(Direccion direccion) {
        return gson.toJson(direccion);
    }
}





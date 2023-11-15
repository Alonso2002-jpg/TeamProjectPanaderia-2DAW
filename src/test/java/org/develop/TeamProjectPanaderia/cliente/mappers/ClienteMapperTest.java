package org.develop.TeamProjectPanaderia.cliente.mappers;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteResponseDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.cliente.mapper.ClienteMapper;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteMapperTest {
    private final ClienteMapper clienteMapper = new ClienteMapper();
    private final Categoria categoriaCliente = new Categoria(1L, "CLIENTE_TEST", LocalDate.now(), LocalDate.now(),true);
    private final Cliente cliente1 =
            Cliente.builder()
                    .id(1L)
                    .nombreCompleto("TEST1")
                    .correo("test1@gmail.com")
                    .dni("03480731A")
                    .telefono("602697979")
                    .imagen("test1.jpg")
                    .categoria(categoriaCliente)
                    .build();

    @Test
    void testToCliente_create() {
        // Arrange
        ClienteCreateDto clienteCreateDto = new ClienteCreateDto("nuevo_cliente","nuevo_cliente@gmail.com","03480731C", "602697985" ,"test3.jpg", categoriaCliente.getNameCategory(), true);


        // Act
       Cliente nuevoCliente = clienteMapper.toCliente(clienteCreateDto, categoriaCliente);

        // Assert
        assertAll(
                () -> assertNull(nuevoCliente.getId()),
                () -> assertEquals(clienteCreateDto.getNombreCompleto(), nuevoCliente.getNombreCompleto()),
                () -> assertEquals(clienteCreateDto.getCorreo(), nuevoCliente.getCorreo()),
                () -> assertEquals(clienteCreateDto.getDni(), nuevoCliente.getDni()),
                () -> assertEquals(clienteCreateDto.getTelefono(), nuevoCliente.getTelefono()),
                () -> assertEquals(clienteCreateDto.getImagen(), nuevoCliente.getImagen()),
                () -> assertEquals(clienteCreateDto.getCategoria(), nuevoCliente.getCategoria().getNameCategory()),
                () -> assertTrue(nuevoCliente.getIsActive()),
                () -> assertNotNull(nuevoCliente.getFechaCreacion()),
                () -> assertNotNull(nuevoCliente.getFechaActualizacion())
        );
    }

    @Test
    void testToCliente_update(){
        // Arrange
        Cliente clienteExistente = cliente1;
        ClienteUpdateDto clienteUpdateDto = ClienteUpdateDto.builder()
                .nombreCompleto("EvelynObando")
                .correo("evelynobando@gmail.com")
                .telefono("722663186")
                .isActive(true)
                .build();

        // Act
        Cliente clienteActualizado = clienteMapper.toCliente(clienteUpdateDto, clienteExistente, categoriaCliente);

        // Assert
        assertAll(
                () -> assertEquals(clienteExistente.getId(), clienteActualizado.getId()),
                () -> assertEquals(clienteUpdateDto.getNombreCompleto(), clienteActualizado.getNombreCompleto()),
                () -> assertEquals(clienteUpdateDto.getCorreo(), clienteActualizado.getCorreo()),
                () -> assertEquals(clienteUpdateDto.getTelefono(), clienteActualizado.getTelefono()),
                () -> assertEquals(clienteExistente.getImagen(), clienteActualizado.getImagen()),
                () -> assertEquals(clienteExistente.getCategoria(), clienteActualizado.getCategoria()),
                () -> assertEquals(clienteExistente.getIsActive(),clienteActualizado.getIsActive()),
                () -> assertEquals(clienteExistente.getFechaCreacion(), clienteActualizado.getFechaCreacion()),
                () -> assertNotEquals(clienteExistente.getFechaActualizacion(), clienteActualizado.getFechaCreacion())
        );
    }

    @Test
    void toResponseDtoTest() {
        // Arrange
        Cliente cliente =  cliente1;

        // Act
        ClienteResponseDto clienteResponseDto =  clienteMapper.toClienteResponseDto(cliente);

        // Assert
        assertAll(
                () -> assertEquals( cliente.getId(),  clienteResponseDto.getId()),
                () -> assertEquals( cliente.getNombreCompleto(),  clienteResponseDto.getNombreCompleto()),
                () -> assertEquals( cliente.getCorreo(), clienteResponseDto.getCorreo()),
                () -> assertEquals( cliente.getDni(), clienteResponseDto.getDni()),
                () -> assertEquals( cliente.getTelefono(),  clienteResponseDto.getTelefono()),
                () -> assertEquals( cliente.getImagen(),  clienteResponseDto.getImagen()),
                () -> assertEquals( cliente.getCategoria().getNameCategory(), clienteResponseDto.getCategoria()),
                () -> assertEquals( cliente.getFechaCreacion(),  clienteResponseDto.getFechaCreacion()),
                () -> assertEquals( cliente.getFechaActualizacion(), clienteResponseDto.getFechaActualizacion()),
                () -> assertEquals(cliente.getIsActive(), clienteResponseDto.getIsActive())
        );
    }
}

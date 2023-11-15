package org.develop.TeamProjectPanaderia.proveedores.services;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.exceptions.ProveedorNotFoundException;
import org.develop.TeamProjectPanaderia.rest.proveedores.exceptions.ProveedorNotSaveException;
import org.develop.TeamProjectPanaderia.rest.proveedores.mapper.ProveedorMapper;
import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.rest.proveedores.repositories.ProveedorRepository;
import org.develop.TeamProjectPanaderia.rest.proveedores.services.ProveedorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProveedorServiceImplTest {
    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private ProveedorMapper proveedorMapper;

    @InjectMocks
    private ProveedorServiceImpl proveedorService;


    @Test
    void saveProveedores() {
        // Arrange
        ProveedorCreateDto proveedorCreateDto = new ProveedorCreateDto();
        when(proveedorRepository.findByNif(eq(proveedorCreateDto.getNif()))).thenReturn(Optional.empty());
        when(categoriaService.findByName(proveedorCreateDto.getTipo())).thenReturn(new Categoria());

        // Act
        Proveedor result = proveedorService.saveProveedores(proveedorCreateDto);

        // Assert
        verify(proveedorRepository, times(1)).save(any());
    }


    @Test
    void saveProveedoresConNifDuplicado() {
        // Arrange
        ProveedorCreateDto proveedorCreateDto = new ProveedorCreateDto();
        when(proveedorRepository.findByNif(proveedorCreateDto.getNif())).thenReturn(Optional.of(new Proveedor()));

        // Act & Assert
        assertThrows(ProveedorNotSaveException.class, () ->
                proveedorService.saveProveedores(proveedorCreateDto)
        );
        verify(proveedorRepository, never()).save(any());
    }

    @Test
    void updateProveedor() {
        // Arrange
        Long proveedorId = 1L;
        ProveedorUpdateDto updateDto = new ProveedorUpdateDto();
        Proveedor existingProveedor = new Proveedor();
        when(proveedorRepository.findById(proveedorId)).thenReturn(Optional.of(existingProveedor));
        // Act
        Proveedor result = proveedorService.updateProveedor(updateDto, proveedorId);
    }


    @Test
    void updateProveedor_ProveedorNotFound() {
        // Arrange
        Long proveedorId = 1L;
        when(proveedorRepository.findById(proveedorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProveedorNotFoundException.class, () ->
                proveedorService.updateProveedor(new ProveedorUpdateDto(), proveedorId)
        );
        verify(proveedorRepository, never()).save(any());
    }

    @Test
    void getProveedoresById() {
        // Arrange
        Long proveedorId = 1L;
        when(proveedorRepository.findById(proveedorId)).thenReturn(Optional.of(new Proveedor()));

        // Act
        Proveedor result = proveedorService.getProveedoresById(proveedorId);

        // Assert
        assertNotNull(result);
    }

    @Test
    void getProveedoresById_ProveedorNotFound() {
        // Arrange
        Long proveedorId = 1L;
        when(proveedorRepository.findById(proveedorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProveedorNotFoundException.class, () ->
                proveedorService.getProveedoresById(proveedorId)
        );
    }

    @Test
    void deleteProveedoresById() {
        // Arrange
        Long proveedorId = 1L;
        when(proveedorRepository.findById(proveedorId)).thenReturn(Optional.of(new Proveedor()));

        // Act & Assert
        assertDoesNotThrow(() -> proveedorService.deleteProveedoresById(proveedorId));
        verify(proveedorRepository, times(1)).deleteById(proveedorId);
    }

    @Test
    void deleteProveedoresById_ProveedorNotFound() {
        // Arrange
        Long proveedorId = 1L;
        when(proveedorRepository.findById(proveedorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProveedorNotFoundException.class, () ->
                proveedorService.deleteProveedoresById(proveedorId)
        );
        verify(proveedorRepository, never()).deleteById(any());
    }

    @Test
    void findProveedoresByNIF() {
        // Arrange
        String nif = "123";
        when(proveedorRepository.findByNif(nif)).thenReturn(Optional.of(new Proveedor()));

        // Act
        Proveedor result = proveedorService.findProveedoresByNIF(nif);

        // Assert
        assertNotNull(result);
    }

    @Test
    void findProveedoresByNIF_ProveedorNotFound() {
        // Arrange
        String nif = "123";
        when(proveedorRepository.findByNif(nif)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProveedorNotFoundException.class, () ->
                proveedorService.findProveedoresByNIF(nif)
        );
    }

    @Test
    void findAll_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        when(proveedorRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(new PageImpl<>(Collections.emptyList()));

        // Act
        Page<Proveedor> result = proveedorService.findAll(Optional.of("nif"), Optional.of("name"), Optional.of(true), Optional.of("tipo"), pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}


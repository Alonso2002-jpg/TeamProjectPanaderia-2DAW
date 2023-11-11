package org.develop.TeamProjectPanaderia.cliente.services;


import org.develop.TeamProjectPanaderia.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;


public interface ClienteService {
    Page<Cliente> findAll(Optional<String> nombreCompleto, Optional<String> producto, Optional<String> categoria, Pageable pageable);
    Cliente findById(Long id);
    Cliente findByNombreCompleto(String nombreCompleto);
    Cliente save(ClienteCreateDto clienteCreateDto);
    Cliente update(Long id, ClienteUpdateDto clienteUpdateDto);
    void deleteById(Long id);

}

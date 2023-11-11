package org.develop.TeamProjectPanaderia.cliente.services;


import org.develop.TeamProjectPanaderia.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;


import java.util.List;


public interface ClienteService {
    List<Cliente> findAll(String categoria,String producto);
    Cliente findById(Long id);
    Cliente save(ClienteCreateDto clienteCreateDto);
    Cliente update(Long id, ClienteUpdateDto clienteUpdateDto);
    Cliente findByName(String nombre);
    void deleteById(Long id);

}

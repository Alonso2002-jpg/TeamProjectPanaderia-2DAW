package org.develop.TeamProjectPanaderia.cliente.services;

import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;

import java.util.List;

public interface ClienteService {
    List<Cliente> findAll(String categoria);
    Cliente findById(Long id);
    Cliente save(ClienteCreateDto cliente);
    Cliente update(Long id,  ClienteUpdateDto cliente);
    void deleteById(Long id);
    void deleteAll();
}

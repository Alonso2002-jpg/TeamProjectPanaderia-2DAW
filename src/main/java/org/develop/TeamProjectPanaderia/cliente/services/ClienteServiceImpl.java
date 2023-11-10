package org.develop.TeamProjectPanaderia.cliente.services;

import org.develop.TeamProjectPanaderia.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService{
    @Override
    public List<Cliente> findAll(String categoria) {
        return null;
    }

    @Override
    public Cliente findById(Long id) {
        return null;
    }

    @Override
    public Cliente save(ClienteCreateDto cliente) {
        return null;
    }

    @Override
    public Cliente update(Long id, ClienteUpdateDto cliente) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteAll() {

    }
}

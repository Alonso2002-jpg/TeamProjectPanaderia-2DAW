package org.develop.TeamProjectPanaderia.cliente.services;


import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.cliente.exceptions.ClienteNotFoundException;
import org.develop.TeamProjectPanaderia.cliente.mapper.ClienteMapper;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.cliente.repositories.ClienteRepository;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.develop.TeamProjectPanaderia.producto.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;
    private final CategoriaService categoriaService;
    private final ProductoService productoService;
    private final ClienteMapper clienteMapper;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository, CategoriaService categoriaService, ProductoService productoService, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.categoriaService = categoriaService;
        this.productoService = productoService;
        this.clienteMapper = clienteMapper;
    }

    @Override
    public List<Cliente> findAll(String categoria, String producto) {
        if((categoria == null || categoria.isEmpty()) && (producto == null ||producto.isEmpty())){
            log.info("Buscando todos los Clientes: ");
            return clienteRepository.findAll();
        }
        if((categoria != null && !categoria.isEmpty()) && (producto == null || producto.isEmpty())) {
            log.info("Buscando todos los Clientes por categoria: " + categoria);
            return clienteRepository.findAllByCategoriaContainsIgnoreCase(categoria);
        }
        if((categoria == null || categoria.isEmpty())){
            log.info("Buscando todos los clientes por producto: " + producto);
            return clienteRepository.findAllByProductoContainsIgnoreCase(producto);
        }
        log.info("Buscando todos los clientes por categoria: " + categoria + " y producto: " +producto);
        return clienteRepository.findAllByCategoriaContainsIgnoreCaseAndProductoContainsIgnoreCase(categoria, producto);
    }

    @Override
    public Cliente findById(Long id) {
        log.info("Buscando cliente por id: " + id);
        return clienteRepository.findById(id).orElseThrow(() -> new ClienteNotFoundException(id));
    }

    @Override
    public Cliente save(ClienteCreateDto clienteCreateDto) {
        log.info("Guardando cliente: " + clienteCreateDto);
        Categoria categoria = categoriaService.findByName(clienteCreateDto.getCategoria());
        Producto producto = productoService.findByName(clienteCreateDto.getProducto());
        return clienteRepository.save(clienteMapper.toCliente(clienteCreateDto, categoria, producto));
    }


    @Override
    public Cliente update(Long id, ClienteUpdateDto clienteUpdateDto) {
        log.info("Actualizando cliente por id: " + id);
        Cliente clienteActual = this.findById(id);
        Categoria categoria = null;
        Producto producto = null;
        if(clienteUpdateDto.getCategoria() != null && !clienteUpdateDto.getCategoria().isEmpty()){
            categoria = categoriaService.findByName(clienteUpdateDto.getCategoria());
        } else {
            categoria = clienteActual.getCategoria();
        }
        if(clienteUpdateDto.getProducto() != null && !clienteUpdateDto.getProducto().isEmpty()){
            producto = productoService.findByName(clienteUpdateDto.getProducto());
        } else {
            producto = clienteActual.getProducto();
        }
        return clienteRepository.save(clienteMapper.toCliente(clienteUpdateDto, clienteActual, categoria, producto));
    }

    @Override
    public Cliente findByName(String nombre) {
        log.info("Buscando cliente por nombre: " + nombre);
        return clienteRepository.findByNombreEqualsIgnoreCase(nombre).orElseThrow(() -> new ClienteNotFoundException(nombre));
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Borrando cliente por id: " + id);
        this.findById(id);
        clienteRepository.deleteById(id);
    }
}

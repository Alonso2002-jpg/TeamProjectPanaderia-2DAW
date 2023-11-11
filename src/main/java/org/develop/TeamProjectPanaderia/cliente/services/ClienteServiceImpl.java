package org.develop.TeamProjectPanaderia.cliente.services;


import jakarta.persistence.criteria.Join;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;


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
        public Page<Cliente> findAll(Optional<String> nombreCompleto, Optional<String> producto, Optional<String> categoria, Pageable pageable){

            // Criteerio de búsqueda por nombreCompleto
            Specification<Cliente> specNombreCompleto = (root, query, criteriaBuilder) ->
                    nombreCompleto.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombreCompleto")), "%" + n.toLowerCase() + "%"))
                            .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));


            // Criterio de busqueda por producto
            Specification<Cliente> specProducto = (root, query, criteriaBuilder) ->
                    producto.map(c ->{
                        Join<Cliente, Producto> categoriaJoin = root.join("producto");
                        return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("nameProducto")), "%" + c.toLowerCase() + "%");
                    }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

            // Criterio de busqueda por categoria
            Specification<Cliente> specCategoria = (root, query, criteriaBuilder) ->
                    categoria.map(c ->{
                        Join<Cliente, Categoria> categoriaJoin = root.join("categoria");
                        return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("nameCategoria")), "%" + c.toLowerCase() + "%");
                    }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

            Specification<Cliente> criterio = Specification.where(specNombreCompleto)
                    .and(specProducto)
                    .and(specCategoria);
            return clienteRepository.findAll(criterio, pageable);
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
    public Cliente findByNombreCompleto(String nombreCompleto) {
        log.info("Buscando cliente por nombre completo: " + nombreCompleto);
        return clienteRepository.findByNombreCompletoEqualsIgnoreCase(nombreCompleto).orElseThrow(() -> new ClienteNotFoundException(nombreCompleto));
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Borrando cliente por id: " + id);
        Cliente clienteActual = this.findById(id);
        clienteRepository.deleteById(clienteActual.getId());
    }
}

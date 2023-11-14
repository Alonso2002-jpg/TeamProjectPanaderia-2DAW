package org.develop.TeamProjectPanaderia.cliente.services;


import org.develop.TeamProjectPanaderia.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import java.util.Optional;


public interface ClienteService {
    Page<Cliente> findAll(Optional<String> nombreCompleto, Optional<String> categoria, Pageable pageable);
    Cliente findById(Long id);
    Cliente findByDni(String dni);
    Cliente save(ClienteCreateDto clienteCreateDto);
    Cliente update(Long id, ClienteUpdateDto clienteUpdateDto);
    Cliente updateImg(Long id, MultipartFile file);
    void deleteById(Long id);

}

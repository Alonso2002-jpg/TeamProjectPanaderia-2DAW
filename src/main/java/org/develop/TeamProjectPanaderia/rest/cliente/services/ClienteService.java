package org.develop.TeamProjectPanaderia.rest.cliente.services;


import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;


public interface ClienteService {
    Page<Cliente> findAll(Optional<String> nombreCompleto, Optional<String> categoria, Pageable pageable);
    Cliente findById(Long id);
    Cliente findByDni(String dni);
    Cliente save(ClienteCreateDto clienteCreateDto);
    Cliente update(Long id, ClienteUpdateDto clienteUpdateDto);
    Cliente updateImg(Long id, MultipartFile file);
    void deleteById(Long id);
    List<Cliente> findByActiveIs(Boolean isActive);

}

package org.develop.TeamProjectPanaderia.rest.cliente.repositories;

import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> , JpaSpecificationExecutor<Cliente> {

    @Override
    Optional<Cliente> findById(Long id);

    Optional<Cliente> findClienteByDniEqualsIgnoreCase(String dni);

    @Override
    void deleteById(Long id);

    List<Cliente> findByIsActive(boolean isActive);
}
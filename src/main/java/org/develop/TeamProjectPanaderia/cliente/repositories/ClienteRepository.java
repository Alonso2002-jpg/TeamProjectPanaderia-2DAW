package org.develop.TeamProjectPanaderia.cliente.repositories;

import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;



@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> , JpaSpecificationExecutor<Cliente> {

    @Override
    Optional<Cliente> findById(Long id);

    Optional<Cliente> findClienteByDniEqualsIgnoreCase(String dni);

    @Override
    void deleteById(Long id);
}
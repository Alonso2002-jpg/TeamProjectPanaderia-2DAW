package org.develop.TeamProjectPanaderia.cliente.repositories;

import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findClienteByDniEqualsIgnoreCase(String dni);
}

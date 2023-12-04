package org.develop.TeamProjectPanaderia.rest.auth.repositories;

import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Interfaz de repositorio para gestionar operaciones de base de datos relacionadas con la autenticacion de usuarios.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Repository
public interface AuthRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario del usuario que se desea buscar.
     * @return Un objeto Optional que contiene el usuario si se encuentra, o un Optional vacio si no.
     */
    Optional<User> findByUsername(String username);
}

package org.develop.TeamProjectPanaderia.rest.users.repositories;

import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio que gestiona las operaciones de base de datos para la entidad User.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {

    /**
     * Busca un usuario por su nombre de usuario (ignorando mayúsculas y minúsculas).
     *
     * @param username El nombre de usuario del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    Optional<User> findByUsernameIgnoreCase(String username);

    /**
     * Busca un usuario por su dirección de correo electrónico.
     *
     * @param email La dirección de correo electrónico del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca un usuario por su nombre de usuario (ignorando mayúsculas y minúsculas) o por su dirección de correo electrónico (ignorando mayúsculas y minúsculas).
     *
     * @param username El nombre de usuario del usuario a buscar.
     * @param email    La dirección de correo electrónico del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    /**
     * Busca todos los usuarios que coinciden con un nombre de usuario (ignorando mayúsculas y minúsculas).
     *
     * @param username El nombre de usuario de los usuarios a buscar.
     * @return Una lista de usuarios que coinciden con el nombre de usuario proporcionado.
     */
    List<User> findAllByUsernameIgnoreCase(String username);

    /**
     * Actualiza el estado de activación de un usuario a falso por su ID.
     *
     * @param id El identificador único del usuario a desactivar.
     */
    @Modifying
    @Query("UPDATE User p SET p.isActive = false WHERE p.id = :id")
    void updateIsActiveToFalseById(Long id);
}

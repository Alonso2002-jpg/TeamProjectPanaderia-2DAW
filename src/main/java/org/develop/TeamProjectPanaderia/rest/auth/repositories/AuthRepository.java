package org.develop.TeamProjectPanaderia.rest.auth.repositories;

import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

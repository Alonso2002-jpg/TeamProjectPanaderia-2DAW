package org.develop.TeamProjectPanaderia.personal.repositories;

import org.develop.TeamProjectPanaderia.personal.models.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonalRepository extends JpaRepository<Personal, Long> {

    Optional<Personal> findByUUID(UUID aLong);

}

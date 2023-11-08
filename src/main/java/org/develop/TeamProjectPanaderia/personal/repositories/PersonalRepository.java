package org.develop.TeamProjectPanaderia.personal.repositories;

import org.develop.TeamProjectPanaderia.personal.models.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalRepository extends JpaRepository<Personal, Long> {
    @Override
    Optional<Personal> findById(Long aLong);

}

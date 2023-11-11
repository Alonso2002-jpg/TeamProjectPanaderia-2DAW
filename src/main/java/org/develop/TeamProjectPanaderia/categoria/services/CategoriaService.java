package org.develop.TeamProjectPanaderia.categoria.services;

import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    Page<Categoria> findAll(Optional<Boolean> isActive, Optional<String> name, Pageable pageable);
    Categoria findById(Long id);
    Categoria save(CategoriaCreateDto categoria);
    Categoria update(Long id,CategoriaUpdateDto categoria);
    Categoria findByName(String name);
    List<Categoria> findByActiveIs(boolean isActive);
    void deleteById(Long id);
    void deleteAll();
}

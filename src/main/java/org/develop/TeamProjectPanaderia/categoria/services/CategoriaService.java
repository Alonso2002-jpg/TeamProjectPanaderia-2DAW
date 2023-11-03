package org.develop.TeamProjectPanaderia.categoria.services;

import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;

import java.util.List;

public interface CategoriaService {
    List<Categoria> findAll();
    Categoria findById(Long id);
    Categoria save(CategoriaCreateDto categoria);
    Categoria update(Long id,CategoriaUpdateDto categoria);
    void deleteById(Long id);
    void deleteAll();
}

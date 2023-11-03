package org.develop.TeamProjectPanaderia.categoria.services;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;

import java.util.List;

public interface CategoriaService {
    List<Categoria> findAll();
    Categoria findById(Long id);
    Categoria save(Categoria categoria);
    Categoria update(Categoria categoria);
    void deleteById(Long id);
    void deleteAll();
}

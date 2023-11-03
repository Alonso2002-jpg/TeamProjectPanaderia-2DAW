package org.develop.TeamProjectPanaderia.categoria.services;

import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoriaServiceImpl implements CategoriaService{
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("No existe la categoria con id: " + id));
    }

    @Override
    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria update(Categoria categoria) {
        var categoriaUpd = findById(categoria.getId());
        return categoriaRepository.save(categoriaUpd);
    }

    @Override
    public void deleteById(Long id) {
        var categoria = findById(id);
        categoriaRepository.delete(categoria);
    }

    @Override
    public void deleteAll() {
        categoriaRepository.deleteAll();
    }
}

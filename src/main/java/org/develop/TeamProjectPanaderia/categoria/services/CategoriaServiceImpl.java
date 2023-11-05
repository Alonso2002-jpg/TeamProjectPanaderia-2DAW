package org.develop.TeamProjectPanaderia.categoria.services;

import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.categoria.exceptions.CategoriaNotFoundException;
import org.develop.TeamProjectPanaderia.categoria.exceptions.CategoriaNotSaveException;
import org.develop.TeamProjectPanaderia.categoria.mapper.CategoriaMapper;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoriaServiceImpl implements CategoriaService{
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    @Override
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new CategoriaNotFoundException(id));
    }

    @Override
    public Categoria save(CategoriaCreateDto categoria) {
        if (categoriaRepository.findByNameCategoryIgnoreCase(categoria.nameCategory()).isPresent()){
            throw new CategoriaNotSaveException("Category already exists");
        }
        return categoriaRepository.save(categoriaMapper.toCategoria(categoria));
    }

    @Override
    public Categoria update(Long id,CategoriaUpdateDto categoria) {
        var categoriaUpd = findById(id);
        return categoriaRepository.save(categoriaMapper.toCategoria(categoria,categoriaUpd));
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

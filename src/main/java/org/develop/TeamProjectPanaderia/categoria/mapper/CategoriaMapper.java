package org.develop.TeamProjectPanaderia.categoria.mapper;

import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoriaMapper {
    public Categoria toCategoria(CategoriaCreateDto dto) {
        return Categoria.builder()
                .nameCategory(dto.nameCategory())
                .isActive(dto.isActive()!= null ? dto.isActive() : true)
                .build();
    }

    public CategoriaCreateDto toCreate(Categoria categoria){
        return new CategoriaCreateDto(
                categoria.getNameCategory(),
                categoria.isActive()
        );
    }

    public Categoria toCategoria(CategoriaUpdateDto update, Categoria categoria){
        return Categoria.builder()
                .id(categoria.getId())
                .nameCategory(update.nameCategory() == null ? categoria.getNameCategory() : update.nameCategory())
                .isActive(update.isActive() == null ? categoria.isActive() : update.isActive())
                .build();
    }

    public CategoriaUpdateDto toUpdate(Categoria categoria){
        return new CategoriaUpdateDto(
                categoria.getNameCategory(),
                categoria.isActive()
        );
    }

    public Categoria toCategoria(CategoriaResponseDto dto, Long id){
        return Categoria.builder()
                .id(id)
                .nameCategory(dto.nameCategory())
                .isActive(dto.isActive())
                .build();
    }

    public CategoriaResponseDto toResponse(Categoria categoria){
        return new CategoriaResponseDto(
                categoria.getNameCategory(),
                categoria.getCreatedAt().toString(),
                categoria.getUpdatedAt().toString(),
                categoria.isActive()
        );
    }

    public List<CategoriaResponseDto> toResponseList(List<Categoria> categorias){
        return categorias.stream()
                .map(this::toResponse)
                .toList();
    }
}

package org.develop.TeamProjectPanaderia.rest.categoria.mapper;

import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Componente encargado de realizar la conversion entre objetos relacionados con la entidad Categoria.
 */
@Component
public class CategoriaMapper {
    /**
     * Convierte un objeto CategoriaCreateDto a una entidad Categoria.
     *
     * @param dto Objeto CategoriaCreateDto.
     * @return Entidad Categoria.
     */
    public Categoria toCategoria(CategoriaCreateDto dto) {
        return Categoria.builder()
                .nameCategory(dto.nameCategory())
                .isActive(dto.isActive()!= null ? dto.isActive() : true)
                .build();
    }
    /**
     * Convierte una entidad Categoria a un objeto CategoriaCreateDto.
     *
     * @param categoria Entidad Categoria.
     * @return Objeto CategoriaCreateDto.
     */
    public CategoriaCreateDto toCreate(Categoria categoria){
        return new CategoriaCreateDto(
                categoria.getNameCategory(),
                categoria.isActive()
        );
    }
    /**
     * Actualiza una entidad Categoria con la informacion de un objeto CategoriaUpdateDto.
     *
     * @param update    Objeto CategoriaUpdateDto.
     * @param categoria Entidad Categoria a actualizar.
     * @return Entidad Categoria actualizada.
     */
    public Categoria toCategoria(CategoriaUpdateDto update, Categoria categoria){
        return Categoria.builder()
                .id(categoria.getId())
                .nameCategory(update.nameCategory() == null ? categoria.getNameCategory() : update.nameCategory())
                .isActive(update.isActive() == null ? categoria.isActive() : update.isActive())
                .build();
    }
    /**
     * Convierte una entidad Categoria a un objeto CategoriaUpdateDto.
     *
     * @param categoria Entidad Categoria.
     * @return Objeto CategoriaUpdateDto.
     */
    public CategoriaUpdateDto toUpdate(Categoria categoria){
        return new CategoriaUpdateDto(
                categoria.getNameCategory(),
                categoria.isActive()
        );
    }
    /**
     * Convierte un objeto CategoriaResponseDto y un ID a una entidad Categoria.
     *
     * @param dto Objeto CategoriaResponseDto.
     * @param id  ID de la entidad Categoria.
     * @return Entidad Categoria.
     */
    public Categoria toCategoria(CategoriaResponseDto dto, Long id){
        return Categoria.builder()
                .id(id)
                .nameCategory(dto.nameCategory())
                .isActive(dto.isActive())
                .build();
    }
    /**
     * Convierte una entidad Categoria a un objeto CategoriaResponseDto.
     *
     * @param categoria Entidad Categoria.
     * @return Objeto CategoriaResponseDto.
     */
    public CategoriaResponseDto toResponse(Categoria categoria){
        return new CategoriaResponseDto(
                categoria.getNameCategory(),
                categoria.getCreatedAt().toString(),
                categoria.getUpdatedAt().toString(),
                categoria.isActive()
        );
    }
    /**
     * Convierte una lista de entidades Categoria a una lista de objetos CategoriaResponseDto.
     *
     * @param categorias Lista de entidades Categoria.
     * @return Lista de objetos CategoriaResponseDto.
     */
    public List<CategoriaResponseDto> toResponseList(List<Categoria> categorias){
        return categorias.stream()
                .map(this::toResponse)
                .toList();
    }
    /**
     * Convierte una página de entidades Categoria a una página de objetos CategoriaResponseDto.
     *
     * @param pageCategoria Página de entidades Categoria.
     * @return Página de objetos CategoriaResponseDto.
     */
    public Page<CategoriaResponseDto> toPageResponse(Page<Categoria> pageCategoria){
        return pageCategoria.map(this::toResponse);
    }
}

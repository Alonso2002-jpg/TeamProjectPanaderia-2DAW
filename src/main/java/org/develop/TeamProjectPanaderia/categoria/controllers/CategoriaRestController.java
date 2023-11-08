package org.develop.TeamProjectPanaderia.categoria.controllers;

import jakarta.validation.Valid;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.categoria.mapper.CategoriaMapper;
import org.develop.TeamProjectPanaderia.categoria.repositories.CategoriaRepository;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categoria")
public class CategoriaRestController {
    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    @Autowired
    public CategoriaRestController(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDto>> findAll(@RequestParam(required = false) boolean isActive){
        var categorias = this.categoriaService.findAll(isActive);
        return ResponseEntity.ok(categoriaMapper.toResponseList(categorias));
    }

    @GetMapping("/{name}")
    public ResponseEntity<CategoriaResponseDto> findByName(@PathVariable String name){
        var categoria = this.categoriaService.findByName(name);
        return ResponseEntity.ok(categoriaMapper.toResponse(categoria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> findById(@PathVariable Long id){
        var categoria = this.categoriaService.findById(id);
        return ResponseEntity.ok(categoriaMapper.toResponse(categoria));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDto> postCategoria(@Valid @RequestBody CategoriaCreateDto categoria){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaMapper.toResponse(categoriaService.save(categoria)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> putCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaUpdateDto categoria){
        return ResponseEntity.ok(categoriaMapper.toResponse(categoriaService.update(id,categoria)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

package org.develop.TeamProjectPanaderia.Proveedores.dto;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;

import java.time.LocalDate;

public class ProveedoresResponseDto {
    private Long id;
    private String NIF;
    private Categoria Tipo;
    private String numero;
    private String nombre;
    private LocalDate fechaCreacion;
    private LocalDate fechaUpdate;

    public ProveedoresResponseDto(Long id, String nif, Categoria tipo, String numero, String nombre, LocalDate fechaCreacion, LocalDate fechaUpdate) {
    }
}

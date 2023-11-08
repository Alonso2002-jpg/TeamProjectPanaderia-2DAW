package org.develop.TeamProjectPanaderia.Proveedores.dto;

import lombok.Getter;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;

import java.time.LocalDate;

public class ProveedoresResponseDto {
    @Getter
    private Long id;
    @Getter
    private String NIF;
    @Getter
    private Categoria Tipo;
    @Getter
    private String numero;
    @Getter
    private String nombre;
    @Getter
    private LocalDate fechaCreacion;
    @Getter
    private LocalDate fechaUpdate;

    public ProveedoresResponseDto(Long id, String NIF, Categoria Tipo, String numero, String nombre, LocalDate fechaCreacion, LocalDate fechaUpdate) {
        this.id = id;
        this.NIF = NIF;
        this.Tipo = Tipo;
        this.numero = numero;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.fechaUpdate = fechaUpdate;
    }

}

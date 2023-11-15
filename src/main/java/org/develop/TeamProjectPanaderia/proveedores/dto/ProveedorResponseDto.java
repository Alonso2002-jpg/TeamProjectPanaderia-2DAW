package org.develop.TeamProjectPanaderia.proveedores.dto;

import lombok.*;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorResponseDto {
    String nif;
    Categoria tipo;
    String numero;
    String nombre;
    Boolean isActive;
    LocalDate fechaCreacion;
    LocalDate fechaUpdate;

}
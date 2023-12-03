package org.develop.TeamProjectPanaderia.rest.proveedores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorResponseDto {
    @Schema(description = "NIF del proveedor", example = "12345678Z")
    String nif;
    @Schema(description = "Tipo del proveedor", example = "Distribuidor")
    Categoria tipo;
    @Schema(description = "Numero de telefono del proveedor", example = "722663189")
    String numero;
    @Schema(description = "Nombre del proveedor", example = "Harinas S.L.")
    String nombre;
    @Schema(description = "Si esta activo o no el proveedor", example = "true")
    Boolean isActive;
    @Schema(description = "Fecha de creacion del registro de proveedor", example = "2023-12-02")
    LocalDate fechaCreacion;
    @Schema(description = "Fecha de actualizacion del registro de proveedor", example = "2023-12-02")
    LocalDate fechaUpdate;

}
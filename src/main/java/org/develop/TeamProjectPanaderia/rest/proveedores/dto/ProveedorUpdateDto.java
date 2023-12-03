package org.develop.TeamProjectPanaderia.rest.proveedores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorUpdateDto {
    @Pattern(message = "El NIF debe tener 8 digitos + 1 letra", regexp = "^\\d{8}[A-Za-z]$")
    @Schema(description = "El NIF del proveedor", example = "12345678Z")
    String nif;
    @Schema(description = "El tipo del proveedor", example = "Distribuidor")
    String tipo;
    @Pattern(message = "El numero debe tener 9 caracteres", regexp = "^[0-9]{9}$")
    @Schema(description = "El numero de telefono del proveedor", example = "722663189")
    String numero;
    @Length(min = 3, message = "El nombre debe tener al menos 8 caracteres")
    @Schema(description = "El nombre del proveedor", example = "Harinas S.L.")
    String nombre;
    @Schema(description = "Si esta activo o no el proveedor", example = "true")
    Boolean isActive;
}

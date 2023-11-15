package org.develop.TeamProjectPanaderia.proveedores.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorUpdateDto {
    @Pattern(message = "El NIF debe tener 8 digitos + 1 letra", regexp = "^\\d{8}[A-Za-z]$")
    String nif;
    String tipo;
    @Pattern(message = "El numero debe tener 9 caracteres", regexp = "^[0-9]{9}$")
    String numero;
    @Length(min = 3, message = "El nombre debe tener al menos 8 caracteres")
    String nombre;
    Boolean isActive;
}

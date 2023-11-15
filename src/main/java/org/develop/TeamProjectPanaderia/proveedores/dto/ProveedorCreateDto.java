package org.develop.TeamProjectPanaderia.proveedores.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorCreateDto {
    @Pattern(message = "El NIF debe tener 8 digitos + 1 letra", regexp = "^\\d{8}[A-Za-z]$")
    @NotBlank(message = "El NIF no puede estar vacio")
    String nif;
    @Length(min = 3, message = "El tipo debe tener al menos 3 caracteres")
    @NotBlank(message = "El tipo no puede estar vacio")
    String tipo;
    @Pattern(message = "El numero debe tener 9 caracteres", regexp = "^[0-9]{9}$")
    @NotBlank(message = "El numero no puede estar vacio")
    String numero;
    @Length(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    @NotBlank(message = "El nombre no puede estar vacio")
    String nombre;
}

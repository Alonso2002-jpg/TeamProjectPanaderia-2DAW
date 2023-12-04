package org.develop.TeamProjectPanaderia.rest.proveedores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 * Clase que representa los datos de creación de un proveedor.
 * Esta clase se utiliza como DTO (Data Transfer Object) para recibir información al crear un proveedor en la aplicación.
 *
 * Se han aplicado anotaciones de Lombok para generar automáticamente métodos como toString, equals, hashCode, y constructores.
 * Además, se han utilizado anotaciones de validación para garantizar que los datos cumplan con ciertos requisitos.
 *
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorCreateDto {
    @Pattern(message = "El NIF debe tener 8 digitos + 1 letra", regexp = "^\\d{8}[A-Za-z]$")
    @NotBlank(message = "El NIF no puede estar vacio")
    @Schema(description = "NIF del proveedor", example = "12345678Z")
    String nif;
    @Length(min = 3, message = "El tipo debe tener al menos 3 caracteres")
    @NotBlank(message = "El tipo no puede estar vacio")
    @Schema(description = "Tipo del proveedor", example = "Distribuidor")
    String tipo;
    @Pattern(message = "El numero debe tener 9 caracteres", regexp = "^[0-9]{9}$")
    @NotBlank(message = "El numero no puede estar vacio")
    @Schema(description = "Numero de telefono del proveedor", example = "722663189")
    String numero;
    @Length(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    @NotBlank(message = "El nombre no puede estar vacio")
    @Schema(description = "Nombre del proveedor", example = "Harinas S.L.")
    String nombre;
}

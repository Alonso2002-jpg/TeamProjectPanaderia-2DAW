package org.develop.TeamProjectPanaderia.rest.proveedores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 * Clase que representa un DTO (Data Transfer Object) para la actualización de proveedores.
 * Se utiliza para recibir datos de actualización de un proveedor desde el cliente hasta el controlador.
 *
 * Se han aplicado anotaciones de Lombok para generar automáticamente métodos como toString, equals, hashCode, y constructores.
 * Además, se han utilizado anotaciones de Swagger para proporcionar información adicional sobre cada campo en la documentación de la API.
 *
 * @see lombok.Builder Se utiliza para generar un constructor de tipo "builder" para la clase.
 * @see lombok.Data Se utiliza para generar automáticamente los métodos toString, equals, hashCode, y constructores.
 * @see lombok.AllArgsConstructor Se utiliza para generar un constructor que acepta todos los campos como parámetros.
 * @see lombok.NoArgsConstructor Se utiliza para generar un constructor sin argumentos.
 * @see io.swagger.v3.oas.annotations.media.Schema Se utiliza para proporcionar información adicional sobre cada campo, como descripción y ejemplo, en la documentación de la API.
 */
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

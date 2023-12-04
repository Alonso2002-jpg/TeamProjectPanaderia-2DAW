package org.develop.TeamProjectPanaderia.rest.proveedores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;

import java.time.LocalDate;

/**
 * Clase que representa la respuesta de un proveedor.
 * Esta clase se utiliza como DTO (Data Transfer Object) para enviar información sobre un proveedor desde el servicio hasta el controlador y, finalmente, al cliente.
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
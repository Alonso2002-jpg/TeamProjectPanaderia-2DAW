package org.develop.TeamProjectPanaderia.rest.cliente.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Direccion;


import java.time.LocalDateTime;
/**
 * {@code ClienteResponseDto} es un objeto de transferencia de datos (DTO) que representa la información
 * que se devuelve al obtener detalles de un cliente en la tienda.
 *
 * @AllArgsConstructor Anotación de Lombok que genera automáticamente un constructor que acepta todos
 * los campos de la clase como parámetros.
 * @NoArgsConstructor Anotación de Lombok que genera automáticamente un constructor sin argumentos.
 * @Data Anotación de Lombok que genera automáticamente los métodos 'toString', 'equals', 'hashCode',
 * y los métodos getter y setter para todos los campos de la clase.
 * @Builder Anotación de Lombok que proporciona un patrón de creación de objetos fluido y legible.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClienteResponseDto {
    @Schema(description = "Identificador unico del cliente", example = "1")
    private Long id;
    @Schema(description = "Nombre completo del cliente", example = "Kurt Cobain")
    private String nombreCompleto;
    @Schema(description = "Correo del cliente", example = "kurtcobain@gmail.com")
    private String correo;
    @Schema(description = "DNI del cliente", example = "23456789Z")
    private String dni;
    @Schema(description = "Telefone del cliente", example = "722663189")
    private String telefono;
    @Schema(description = "Imagen del cliente", example = "https://www.realmadrid.com/img/vertical_380px/cristiano_550x650_20180917025046.jpg")
    private String imagen;
    @Schema(description = "Fecha de creacion del registro del cliente", example = "")
    private LocalDateTime fechaCreacion;
    @Schema(description = "Fecha de actualizacion del registro del cliente", example = "")
    private LocalDateTime fechaActualizacion;
    @Schema(description = "Categoria del cliente", example = "VIP")
    private String categoria;
    @Schema(description = "Si esta activo o no el cliente", example = "true")
    private Boolean isActive;
    private Direccion direccion;
}

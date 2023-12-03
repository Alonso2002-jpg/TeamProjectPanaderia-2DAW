package org.develop.TeamProjectPanaderia.rest.cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Direccion;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
public class ClienteUpdateDto {
    @Length(min = 8, message = "El nombre debe tener al menos 8 caracteres")
    @Schema(description = "El nombre completo del cliente", example = "Kurt Cobain")
    private String nombreCompleto;
    @Pattern(regexp = ".*@.*\\..*", message = "El correo debe contener al menos un '@' y al menos un '.'")
    @Schema(description = "El correo del cliente", example = "kurtcobain@gmail.com")
    private String correo;
    @Pattern(regexp = "^[679][0-9]{8,}$", message = "El teléfono debe comenzar con 9, 6 o 7 y tener  9 números")
    @Schema(description = "Telefono del cliente", example = "722663189")
    private String telefono;
    @Schema(description = "Imagen del cliente", example = "https://www.realmadrid.com/img/vertical_380px/cristiano_550x650_20180917025046.jpg")
    private String imagen;
    @Schema(description = "Categoria del cliente", example = "VIP")
    private String categoria;
    private Direccion direccion;
    @Schema(description = "Si esta activo o no el cliente", example = "true")
    private Boolean isActive;
}

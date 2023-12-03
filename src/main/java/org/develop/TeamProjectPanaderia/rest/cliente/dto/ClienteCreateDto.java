package org.develop.TeamProjectPanaderia.rest.cliente.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Direccion;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class ClienteCreateDto  {
    @NotBlank( message = "El nombre no puede estar vacio")
    @Length(min = 8, message = "El nombre debe tener al menos 8 caracteres")
    @Schema(description = "Nombre completo del cliente", example = "Kurt Cobain")
    private String nombreCompleto;
    @NotBlank(message = "El correo no puede estar vacio")
    @Pattern(regexp = ".*@.*\\..*", message = "El correo debe contener al menos un '@' y al menos un '.'")
    @Schema(description = "Correo del cliente", example = "kurtcobain@gmail.com")
    private String correo;
    @NotBlank( message = "El DNI no puede estar vacia")
    @Pattern(regexp = "^[0-9]{8}[a-zA-Z]$", message = "El DNI debe tener 8 números seguidos de una letra")
    @Schema(description = "DNI del cliente", example = "23456789A")
    private String dni;
    @Pattern(regexp = "^[679][0-9]{8,}$", message = "El teléfono debe comenzar con 9, 6 o 7 y tener  9 números")
    @Schema(description = "Telefono del cliente", example = "722663189")
    private String telefono;
    @Schema(description = "Imagen del cliente", example = Cliente.IMAGE_DEFAULT)
    private String imagen;
    private Direccion direccion;
    @NotBlank(message = "La categoria no puede estar vacio")
    @Schema(description = "Categoria del cliente", example = "VIP")
    private String categoria;
    @Schema(description = "Si esta activo o no el cliente", example = "true")
    private Boolean isActive;
}

package org.develop.TeamProjectPanaderia.cliente.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ClienteUpdateDto {

    @Length(min = 8, message = "El nombre debe tener al menos 8 caracteres")
    private String nombreCompleto;
    @Pattern(regexp = ".*@.*\\..*", message = "El correo debe contener al menos un '@' y al menos un '.'")
    private String correo;
    @Pattern(regexp = "^[679][0-9]{8,}$", message = "El teléfono debe comenzar con 9, 6 o 7 y tener  9 números")
    private String telefono;
    private String imagen;
    private String producto;
    private String categoria;
}

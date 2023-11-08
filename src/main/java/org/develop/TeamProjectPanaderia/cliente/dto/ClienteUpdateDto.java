package org.develop.TeamProjectPanaderia.cliente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class ClienteUpdateDto {

    @Length(min = 8, message = "El nombre debe tener al menos 8 caracteres")
    private String nombreCompleto;
    @Pattern(regexp = ".*@.*\\..*", message = "El correo debe contener al menos un '@' y al menos un '.'")
    private String correo;
    @Pattern(regexp = "^[0-9]{8}[a-zA-Z]$", message = "El DNI debe tener 8 números seguidos de una letra")
    private String dni;
    @Pattern(regexp = "^[679][0-9]{8,}$", message = "El teléfono debe comenzar con 9, 6 o 7 y tener  9 números")
    private String telefono;
    private String producto;
    private String categoria;
}

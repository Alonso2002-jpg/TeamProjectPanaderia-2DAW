package org.develop.TeamProjectPanaderia.rest.cliente.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record Direccion(
        @Length(min = 3, message = "La calle debe tener al menos 3 caracteres")
        @NotBlank(message = "La calle no puede estar vacia")
        String calle,
        @NotBlank(message = "El numero no puede estar vacio")
        String numero,
        @NotBlank(message = "La ciudad no puede estar vacia")
        @Length(min = 3, message = "La ciudad debe tener al menos 3 caracteres")
        String ciudad,
        @NotBlank(message = "La provincia no puede estar vacia")
        @Length(min = 3, message = "La provincia debe tener al menos 3 caracteres")
        String provincia,
        @NotBlank(message = "El pais no puede estar vacio")
        @Length(min = 3, message = "El pais debe tener al menos 3 caracteres")
        String pais,
        @NotBlank(message = "El codigo postal no puede estar vacio")
        @Pattern(regexp = "^[0-9]{5}$", message = "El codigo postal debe tener 5 digitos")
        String codPostal
) {
}

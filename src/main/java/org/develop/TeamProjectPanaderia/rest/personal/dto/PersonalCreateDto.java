
package org.develop.TeamProjectPanaderia.rest.personal.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import org.hibernate.validator.constraints.Length;

@Builder
public record PersonalCreateDto(
        @NotBlank(message = "El dni no puede estar vacio")
        @Pattern(regexp = "^[0-9]{8}[a-zA-Z]$", message = "El DNI debe tener 8 numeros seguidos de una letra")
        String dni,
        @Length(min = 3, message = "El nombre debe contener al menos 3 letras")
        @NotBlank(message = "El nombre no puede estar vacio")
        String nombre,
        @Email(message = "El email no es valido")
        @NotBlank(message = "El email no puede estar vacio")
        String email,
        @NotBlank(message = "La seccion no puede estar vacia")
        String seccion,
        Boolean isActive
) {
}


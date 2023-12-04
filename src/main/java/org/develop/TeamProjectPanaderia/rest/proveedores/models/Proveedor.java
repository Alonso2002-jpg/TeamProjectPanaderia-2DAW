package org.develop.TeamProjectPanaderia.rest.proveedores.models;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * Clase que representa a un proveedor en el sistema.
 * Contiene información como el identificador único, NIF, tipo, número de teléfono, nombre,
 * estado de activación, fecha de creación y fecha de actualización del registro del proveedor.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PROVEEDORES")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico del proveedor", example = "1")
    private Long id;
    @Column(name = "nif", unique = true, nullable = false)
    @Pattern(message = "El NIF debe tener 8 digitos + 1 letra", regexp = "^\\d{8}[A-Za-z]$")
    @NotBlank(message = "El NIF no puede estar vacio")
    @Schema(description = "El NIF del proveedor", example = "12345678Z")
    private String nif;
    @ManyToOne
    @JoinColumn(name = "tipo")
    @Schema(description = "El tipo del proveedor", example = "Distribuidor")
    private Categoria tipo;
    @Column(name = "numero", unique = true)
    @Pattern(message = "El numero debe tener 9 caracteres", regexp = "^[0-9]{9}$")
    @Schema(description = "El numero de telefono del proveedor", example = "722663189")
    private String numero;
    @Column(name = "name", nullable = false)
    @NotBlank(message = "El nombre no puede estar vacio")
    @Length(min = 3, message = "El nombre debe tener al menos 8 caracteres")
    @Schema(description = "Nombre del proveedor", example = "Harinas S.L.")
    private String nombre;
    @NotNull
    @Builder.Default
    @Column(name = "isActive", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Schema(description = "Si esta activo o no el proveedor", example = "true")
    private Boolean isActive = true;
    @Column(name = "fechaCreacion",updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    @Schema(description = "Fecha de creacion del registro del proveedor", example = "2023-12-02")
    private LocalDate fechaCreacion = LocalDate.now();
    @Column(name = "fechaUpdate", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    @Schema(description = "Fecha de actualizacion del registro del proveedor", example = "2023-12-02")
    private LocalDate fechaUpdate = LocalDate.now();
}

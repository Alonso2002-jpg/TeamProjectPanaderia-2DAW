package org.develop.TeamProjectPanaderia.proveedores.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PROVEEDORES")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nif", unique = true, nullable = false)
    @Pattern(message = "El NIF debe tener 8 digitos + 1 letra", regexp = "^\\d{8}[A-Za-z]$")
    @NotBlank(message = "El NIF no puede estar vacio")
    private String nif;
    @ManyToOne
    @JoinColumn(name = "tipo")
    private Categoria tipo;
    @Column(name = "numero", unique = true)
    @Pattern(message = "El numero debe tener 9 caracteres", regexp = "^[0-9]{9}$")
    private String numero;
    @Column(name = "name", nullable = false)
    @NotBlank(message = "El nombre no puede estar vacio")
    @Length(min = 3, message = "El nombre debe tener al menos 8 caracteres")
    private String nombre;
    @NotNull
    @Builder.Default
    @Column(name = "isActive", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;
    @Column(name = "fechaCreacion",updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDate fechaCreacion = LocalDate.now();
    @Column(name = "fechaUpdate", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDate fechaUpdate = LocalDate.now();
}

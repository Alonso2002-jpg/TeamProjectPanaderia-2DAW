package org.develop.TeamProjectPanaderia.rest.personal.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "PERSONAL")
public class Personal {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "nombre", nullable = false)
    @Length(min = 3, message = "El nombre debe contener al menos 3 letras")
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;
    @Column(name = "dni", nullable = false, unique = true)
    @NotBlank(message = "El dni no puede estar vacio")
    @Pattern(regexp = "^[0-9]{8}[a-zA-Z]$", message = "El DNI debe tener 8 numeros seguidos de una letra")
    private String dni;
    @Builder.Default
    @Column(name = "fecha_alta",  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate fechaAlta = LocalDate.now();
    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;
    @Builder.Default
    @Column(name = "fecha_creacion",  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    @Builder.Default
    @Column(name = "fecha_actualizacion", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
    @Column(name = "active")
    @Builder.Default
    private boolean isActive=true;

    @ManyToOne
    @JoinColumn(name = "seccion", nullable = false)
    private Categoria seccion;
}
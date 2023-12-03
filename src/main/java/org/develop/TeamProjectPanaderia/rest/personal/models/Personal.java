package org.develop.TeamProjectPanaderia.rest.personal.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
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
    @Schema(description = "Identificador unico del trabajador", example = "1a70f426-d51f-4a13-ba39-89203f94ed74")
    private UUID id;
    @Column(name = "nombre", nullable = false)
    @Length(min = 3, message = "El nombre debe contener al menos 3 letras")
    @NotBlank(message = "El nombre no puede estar vacio")
    @Schema(description = "Nombre del trabajador", example = "Joselyn Obando")
    private String nombre;
    @Column(name = "dni", nullable = false, unique = true)
    @NotBlank(message = "El dni no puede estar vacio")
    @Pattern(regexp = "^[0-9]{8}[a-zA-Z]$", message = "El DNI debe tener 8 numeros seguidos de una letra")
    @Schema(description = "DNI del trabajador", example = "03480731B")
    private String dni;
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "El email no es valido")
    private String email;
    @Builder.Default
    @Column(name = "fecha_alta",  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de alta del trabajador", example = "2023-12-02")
    private LocalDate fechaAlta = LocalDate.now();
    @Column(name = "fecha_baja")
    @Schema(description = "Fecha de baja del trabajador", example = "")
    private LocalDate fechaBaja;
    @Builder.Default
    @Column(name = "fecha_creacion",  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de creacion del registro del trabajador", example = "2023-12-02")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    @Builder.Default
    @Column(name = "fecha_actualizacion", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de actualizacion del registro del trabajador", example = "2023-12-02")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @Column(name = "active")
    @Builder.Default
    @Schema(description = "Si esta activo o no el trabajador", example = "true")
    private boolean isActive=true;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "seccion", nullable = false)
    @Schema(description = "Seccion del trabajador", example = "Panaderia")
    private Categoria seccion;
}
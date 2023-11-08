package org.develop.TeamProjectPanaderia.personal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;

import java.time.LocalDate;
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
    private String nombre;
    @Column(name = "dni", nullable = false, unique = true)
    private String dni;
    @OneToOne
    @Column(name = "seccion", nullable = false)
    @Builder.Default
    private Categoria seccion= Categoria.builder().nameCategory("EMPLEADO").build();
    @Builder.Default
    @Column(name = "fecha_alta", nullable = false, columnDefinition = "Fecha de alta del personal")
    private LocalDate fechaAlta = LocalDate.now();
    @Builder.Default
    @Column(name = "fecha_baja", nullable = false, columnDefinition = "Fecha de baja del personal")
    private LocalDate fechaBaja= LocalDate.now();
    @Builder.Default
    @Column(name = "fecha_creacion", nullable = false, columnDefinition = "Fecha de creacion del personal")
    private LocalDate fechaCreacion = LocalDate.now();
    @Builder.Default
    @Column(name = "fecha_actualizacion", nullable = false, columnDefinition = "Fecha de actualizacion del personal")
    private LocalDate fechaUpdate=LocalDate.now();
    @Column(name = "active", nullable = false, columnDefinition = "avtivado por defecto true")
    @Builder.Default
    private boolean isActive=true;

}

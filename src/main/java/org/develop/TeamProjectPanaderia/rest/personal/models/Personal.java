package org.develop.TeamProjectPanaderia.rest.personal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;

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
    @ManyToOne
    @JoinColumn(name = "seccion", nullable = false)
    @Builder.Default
    private Categoria seccion= Categoria.builder().nameCategory("EMPLEADO").build();
    @Builder.Default
    @Column(name = "fecha_alta")
    private LocalDate fechaAlta = LocalDate.now();
    @Builder.Default
    @Column(name = "fecha_baja")
    private LocalDate fechaBaja= LocalDate.now();
    @Builder.Default
    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion = LocalDate.now();
    @Builder.Default
    @Column(name = "fecha_actualizacion")
    private LocalDate fechaUpdate=LocalDate.now();
    @Column(name = "active")
    @Builder.Default
    private boolean isActive=true;

}

package org.develop.TeamProjectPanaderia.personal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PERSONAL")
public class Personal {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "dni", nullable = false, unique = true)
    private String dni;
    @OneToOne
    @Column(name = "seccion", nullable = false)
    private Categoria seccion;
    @Builder.Default
    @Column(name = "fecha_alta", nullable = false, columnDefinition = "Fecha de alta del personal")
    private LocalDate fechaAlta;
    @Builder.Default
    @Column(name = "fecha_baja", nullable = false, columnDefinition = "Fecha de baja del personal")
    private LocalDate fechaBaja;
    @Builder.Default
    @Column(name = "fecha_creacion", nullable = false, columnDefinition = "Fecha de creacion del personal")
    private LocalDate fechaCreacion;
    @Builder.Default
    @Column(name = "fecha_actualizacion", nullable = false, columnDefinition = "Fecha de actualizacion del personal")
    private LocalDate fechaUpdate;
    @Column(name = "active", nullable = false, columnDefinition = "avtivado por defecto true")
    private boolean isActive=true;

}

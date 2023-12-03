package org.develop.TeamProjectPanaderia.rest.categoria.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CATEGORIAS")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico de la categoria", example = "1")
    private Long id;
    @Column(name = "name", unique = true, nullable = false)
    @Schema(description = "El nombre de la categoria", example = "VIP")
    private String nameCategory;
    @Builder.Default
    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de creacion del registro de la categoria", example = "2023-12-02")
    private LocalDate createdAt = LocalDate.now();
    @Builder.Default
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de actualizacion del registro de la categoria", example = "2023-12-02")
    private LocalDate updatedAt = LocalDate.now();
    @Builder.Default
    @Column(name = "active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Schema(description = "Si esta activo o no la categoria", example = "true")
    private boolean isActive = true;
}

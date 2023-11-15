package org.develop.TeamProjectPanaderia.rest.categoria.models;

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
    private Long id;
    @Column(name = "name", unique = true, nullable = false)
    private String nameCategory;
    @Builder.Default
    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate createdAt = LocalDate.now();
    @Builder.Default
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDate updatedAt = LocalDate.now();
    @Builder.Default
    @Column(name = "active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;
}

package org.develop.TeamProjectPanaderia.Proveedores.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PROVEEDORES")
public class Proveedores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NIF", unique = true, nullable = false)
    private String NIF;
    @OneToOne
    @JoinColumn(name = "tipo")
    private Categoria Tipo;
    @Column(name = "numero", unique = true)
    private String numero;
    @Column(name = "name", unique = true, nullable = false)
    private String nombre;
    @Column(name = "fechaCreacion")
    private LocalDate fechaCreacion;
    @Column(name = "fechaUpdate")
    private LocalDate fechaUpdate;
}

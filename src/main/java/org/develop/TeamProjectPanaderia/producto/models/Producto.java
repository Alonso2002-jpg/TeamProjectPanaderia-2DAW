package org.develop.TeamProjectPanaderia.producto.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedores;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRODUCTOS")
public class Producto {
    @Builder.Default
    public static final String IMAGE_DEFAULT = "https://via.placeholder.com/150";
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "nombre", unique = true, nullable = false)
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;
    @Column(name = "stock", columnDefinition = "integer default 0")
    @Builder.Default
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock = 0;
    @Column(updatable = false, nullable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    @Column(updatable = true, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
    @Column(columnDefinition = "TEXT default '" + IMAGE_DEFAULT + "'")
    @Builder.Default
    private String imagen = IMAGE_DEFAULT;
    @Column(name = "precio", columnDefinition = "double default 0.0")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double precio = 0.0;
    @Column(columnDefinition = "boolean default true")
    @Builder.Default
    private Boolean isActivo = true;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedores proveedor;
}






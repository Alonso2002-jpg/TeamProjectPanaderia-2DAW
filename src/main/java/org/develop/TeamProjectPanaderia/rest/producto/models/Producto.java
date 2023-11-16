package org.develop.TeamProjectPanaderia.rest.producto.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.hibernate.validator.constraints.Length;

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
    public static final String IMAGE_DEFAULT = "https://www.realmadrid.com/img/vertical_380px/cristiano_550x650_20180917025046.jpg";
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "nombre", unique = true, nullable = false)
    @Length(min = 3, message = "El nombre debe contener al menos 3 letras")
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;
    @Column(name = "stock", columnDefinition = "integer default 0")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    @Column(updatable = false, nullable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;
    @Column(updatable = true, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime fechaActualizacion;
    @Column(columnDefinition = "TEXT default '" + IMAGE_DEFAULT + "'")
    private String imagen;
    @Column(name = "precio", columnDefinition = "double default 0.0")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double precio;
    @Column(columnDefinition = "boolean default true")
    private Boolean isActivo;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
}






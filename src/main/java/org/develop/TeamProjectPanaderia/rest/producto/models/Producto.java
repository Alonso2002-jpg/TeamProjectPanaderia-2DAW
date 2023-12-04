package org.develop.TeamProjectPanaderia.rest.producto.models;

import io.swagger.v3.oas.annotations.media.Schema;
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
/**
 * La clase {@code Producto} representa un producto en la base de datos.
 * Cada producto tiene un identificador único, nombre, stock, fecha de creación,
 * fecha de actualización, imagen, precio, estado de activación, categoría y proveedor asociados.
 * La clase utiliza anotaciones de Lombok para generar automáticamente
 * métodos como getters, setters, constructores y JPA para mapear la clase a una tabla en la base de datos.
 *
 * @version 1.0
 */
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
    @Schema(description = "Identificador unico del producto", example = "1a70f426-d51f-4a13-ba39-89203f94ed74")
    private UUID id;
    @Column(name = "nombre", unique = true, nullable = false)
    @Length(min = 3, message = "El nombre debe contener al menos 3 letras")
    @NotBlank(message = "El nombre no puede estar vacio")
    @Schema(description = "Nombre del producto", example = "Pan de Barra")
    private String nombre;
    @Column(name = "stock", columnDefinition = "integer default 0")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(description = "Cantidad del producto", example = "50")
    private Integer stock;
    @Column(updatable = false, nullable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de creacion del registro", example = "2023-01-01T00:00:00.000Z")
    private LocalDateTime fechaCreacion;
    @Column(updatable = true, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de actualizacion del registro", example = "2023-01-01T00:00:00.000Z")
    private LocalDateTime fechaActualizacion;
    @Column(columnDefinition = "TEXT default '" + IMAGE_DEFAULT + "'")
    @Schema(description = "Imagen del producto", example = "https://www.realmadrid.com/img/vertical_380px/cristiano_550x650_20180917025046.jpg")
    private String imagen;
    @Column(name = "precio", columnDefinition = "double precision default 0.0")
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Schema(description = "Precio del producto", example = "1.99")
    private Double precio;
    @Column(columnDefinition = "boolean default true")
    @Schema(description = "Si esta activo o no el producto", example = "true")
    private Boolean isActivo;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @Schema(description = "Categoria del producto", example = "Pan")
    private Categoria categoria;
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    @Schema(description = "Nif del proveedor del producto", example = "12345678Z")
    private Proveedor proveedor;
}






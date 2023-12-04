package org.develop.TeamProjectPanaderia.rest.cliente.models;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.hibernate.validator.constraints.Length;


import java.time.LocalDateTime;

/**
 * {@code Cliente} es una entidad que representa a un cliente en la tienda.
 *
 * @Data Genera automáticamente los métodos getter, setter, toString, equals y hashCode para la clase.
 * @Builder Patrón de diseño que permite construir objetos Cliente de manera más concisa y legible.
 * @AllArgsConstructor Genera automáticamente un constructor que incluye todos los campos de la clase como parámetros.
 * @NoArgsConstructor Genera automáticamente un constructor sin parámetros.
 * @Entity Indica que esta clase es una entidad JPA y se mapeará a una tabla en la base de datos.
 * @Table Especifica el nombre de la tabla en la base de datos que se utilizará para almacenar los datos de la entidad.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CLIENTES")
public class Cliente {
    @Builder.Default
    public static final String IMAGE_DEFAULT = "https://www.realmadrid.com/img/vertical_380px/cristiano_550x650_20180917025046.jpg";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico del cliente", example = "1")
    private Long id;
    @NotBlank( message = "El nombre no puede estar vacio")
    @Length(min = 8, message = "El nombre debe tener al menos 8 caracteres")
    @Schema(description = "El nombre completo del cliente", example = "Kurt Cobain")
    private String nombreCompleto;
    @NotBlank(message =  "El correo no puede estar vacio")
    @Pattern(regexp = ".*@.*\\..*", message = "El correo debe contener al menos un '@' y al menos un '.'")
    @Schema(description = "El correo del cliente", example = "kurtcobain@gmail.com")
    private String correo;
    @NotBlank(message = "El DNI no puede estar vacio")
    @Pattern(regexp = "^[0-9]{8}[a-zA-Z]$", message = "El DNI debe tener 8 numeros seguidos de una letra")
    @Column(unique = true)
    @Schema(description = "El DNI del cliente", example = "23456789Z")
    private String dni;
    @Column(columnDefinition = "TEXT default ''")
    @Pattern(regexp = "^[679][0-9]{8,}$", message = "El telefono debe comenzar con 9, 6 o 7 y tener  9 numeros")
    @Schema(description = "El telefono del cliente", example = "722663189")
    private String telefono;
    @Column(columnDefinition = "TEXT default '" + IMAGE_DEFAULT + "'")
    @Schema(description = "La imagen del cliente", example = "https://www.realmadrid.com/img/vertical_380px/cristiano_550x650_20180917025046.jpg")
    private String imagen;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de creacion del registro del cliente", example = "2023-01-01T00:00:00.000Z")
    private LocalDateTime fechaCreacion;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = true, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de actualziacion del registro del cliente", example = "2023-01-01T00:00:00.000Z")
    private LocalDateTime fechaActualizacion;
    @Column(columnDefinition = "Boolean default true")
    @Schema(description = "Si esta activo o no el cliente", example = "true")
    private Boolean isActive;
    @NotBlank(message = "La categoria no puede estar vacia")
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @Schema(description = "La categoria del cliente", example = "VIP")
    private Categoria categoria;

}


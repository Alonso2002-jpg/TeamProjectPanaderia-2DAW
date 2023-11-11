package org.develop.TeamProjectPanaderia.cliente.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.hibernate.validator.constraints.Length;


import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CLIENTES")
public class Cliente {
    @Builder.Default
    public static final String IMAGE_DEFAULT = "https://via.placeholder.com/150";
    @Id // Indicamos que es el ID de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank( message = "El nombre no puede estar vacio")
    @Length(min = 8, message = "El nombre debe tener al menos 8 caracteres")
    private String nombreCompleto;
    @NotBlank(message =  "El correo no puede estar vacio")
    @Pattern(regexp = ".*@.*\\..*", message = "El correo debe contener al menos un '@' y al menos un '.'")
    private String correo;
    @NotBlank( message = "El DNI no puede estar vacia")
    @Pattern(regexp = "^[0-9]{8}[a-zA-Z]$", message = "El DNI debe tener 8 números seguidos de una letra")
    private String dni;
    @Column(columnDefinition = "TEXT default ''")
    @Pattern(regexp = "^[679][0-9]{8,}$", message = "El teléfono debe comenzar con 9, 6 o 7 y tener  9 números")
    @Builder.Default
    private String telefono = "";
    @Column(columnDefinition = "TEXT default '" + IMAGE_DEFAULT + "'")
    @Builder.Default
    private String imagen = IMAGE_DEFAULT;
    @Temporal(TemporalType.TIMESTAMP) // Indicamos que es un campo de tipo fecha y hora
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;
    @Temporal(TemporalType.TIMESTAMP) // Indicamos que es un campo de tipo fecha y hora
    @Column(updatable = true, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

}


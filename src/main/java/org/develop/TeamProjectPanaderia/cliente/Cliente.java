package org.develop.TeamProjectPanaderia.cliente.models;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;


import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
public class Cliente {

    private Long id;
    @NotBlank(message =  "El correo no puede estar vacio")
    private String correo;
    @NotBlank( message = "El DNI no puede estar vacia")
    private String dni;
    @NotBlank( message = "El nombre no puede estar vacio")
    private String nombreCompleto;
    private String telefono;
    private String producto;
    private LocalDate fechaCreacion;
    private LocalDate fechaActualizacion;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

}

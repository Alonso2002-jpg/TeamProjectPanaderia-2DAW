package org.develop.TeamProjectPanaderia.rest.pedidos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase que representa un pedido en un sistema de pedidos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document("pedidos")
@TypeAlias("Pedido")
@EntityListeners(AuditingEntityListener.class)
public class Pedido {
    @Id
    @Builder.Default
    private ObjectId id = new ObjectId();
    @NotNull(message = "El Cliente no puede ser Nulo")
    private Long idCliente;
    @NotNull(message = "El Usuario no puede ser Nulo")
    private Long idUsuario;
    @NotNull(message = "El pedido debe tener minimo una linea de pedido")
    private List<LineaPedido> lineasPedido;
    @Builder.Default
    private Integer totalItems = 0;
    @Builder.Default
    private Double total = 0.0;

    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Builder.Default
    private Boolean deleted = false;

    /**
     * Obtiene la representación hexadecimal del identificador del pedido.
     *
     * @return Representación hexadecimal del identificador del pedido.
     */
    @JsonProperty("id")
    public String get_Id(){
        return id.toHexString();
    }

    /**
     * Establece las líneas de pedido del pedido y actualiza los totales en consecuencia.
     *
     * @param lineasPed Lista de líneas de pedido.
     */
    public void setLineasPedido(List<LineaPedido> lineasPed){
        this.lineasPedido = lineasPed;
        this.totalItems = lineasPed != null ? lineasPedido.size() : 0;
        this.total = lineasPed != null ? lineasPedido.stream().mapToDouble(LineaPedido::getTotal).sum() : 0.0;
    }
}

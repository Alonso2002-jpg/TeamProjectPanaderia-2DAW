package org.develop.TeamProjectPanaderia.rest.pedidos.model;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Clase que representa una línea de pedido en un sistema de pedidos.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineaPedido {
    @Builder.Default
    @Min(value = 1, message = "La cantidad del producto debe ser mayor a 0")
    private Integer cantidad = 1;
    private UUID idProducto;
    @Builder.Default
    @Min(value = 0, message = "El precio del Producto debe ser mayor o igual a 0")
    private Double precioProducto = 0.0;
    @Builder.Default
    private Double total = 0.0;

    /**
     * Establece la cantidad de productos y actualiza el total en consecuencia.
     *
     * @param cantidad Nueva cantidad de productos.
     */
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        this.total = this.cantidad * this.precioProducto;
    }

    /**
     * Establece el precio del producto y actualiza el total en consecuencia.
     *
     * @param precioProducto Nuevo precio del producto.
     */
    public void setPrecioProducto(Double precioProducto) {
        this.precioProducto = precioProducto;
        this.total = this.cantidad * this.precioProducto;
    }

    /**
     * Establece el total de la línea de pedido.
     *
     * @param total Nuevo total de la línea de pedido.
     */
    public void setTotal(Double total) {
        this.total = total;
    }

}
